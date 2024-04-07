package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.client.Sender;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.model.GroupMessageInfo;
import com.yjc.platform.constants.Constant;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.mapper.GroupMessageMapper;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.GroupMessage;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.GroupMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements GroupMessageService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private Sender  sender;
    @Override
    public Long send(GroupMessageVO groupMessageVO) {
        Long userId = SessionContext.getSession().getId();
        Group group= groupService.getById(groupMessageVO.getGroupId());
        if(group == null){
            throw new GlobalException("群聊不存在");
        }
        if (group.getDeleted()){
            throw new GlobalException("群聊已解散");
        }

        List<Long> list = groupMemberService.findMemberIdsByGroupId(groupMessageVO.getGroupId());
        if(!list.contains(userId)){
            throw new GlobalException("你不在群聊中");
        }
        GroupMessage groupMessage = BeanUtil.copyProperties(groupMessageVO, GroupMessage.class);
        groupMessage.setSendId(userId);
        save(groupMessage);

        list = list.stream().filter(id -> id != userId).collect(Collectors.toList());
        GroupMessageInfo groupMessageInfo = BeanUtil.copyProperties(groupMessage, GroupMessageInfo.class);
        sender.sendGroupMessage(list,groupMessageInfo);

        return groupMessage.getId();
    }

    @Override
    public void recall(Long id) {
        Long userId = SessionContext.getSession().getId();
        GroupMessage groupMessage = getById(id);
        if(groupMessage == null){
            throw new GlobalException("消息不存在");
        }
        if (groupMessage.getSendId() != userId){
            throw new GlobalException("你不是发送者，无法撤回");
        }
        if(System.currentTimeMillis()-groupMessage.getSendTime().getTime()> Constant.ALLOW_RECALL_TIME){
            throw new GlobalException("已超过两分钟，无法撤回");
        }

        List<Long> list = groupMemberService.findMemberIdsByGroupId(groupMessage.getGroupId());
        if(!list.contains(userId)){
            throw new GlobalException("你不在群聊中");
        }

        groupMessage.setStatus(MessageStatus.RECALL.code());
        updateById(groupMessage);

        list = list.stream().filter(uid -> id != userId).collect(Collectors.toList());
        GroupMessageInfo groupMessageInfo = BeanUtil.copyProperties(groupMessage, GroupMessageInfo.class);
        GroupMember member = groupMemberService.findByGroupIdAndUserId(groupMessage.getGroupId(), userId);
        String content = member.getNicknameInGroup()+"撤回了一条消息";
        if(member.getNicknameInGroup().equals("")) content = member.getMemberNickname()+"撤回了一条消息";
        groupMessageInfo.setContent(content);
        groupMessageInfo.setType(MessageType.TIP.getCode());
        groupMessageInfo.setSendTime(new Date());
        sender.sendGroupMessage(list,groupMessageInfo);

    }

    @Override
    public void pullUnreadMessage() {
        Long userId = SessionContext.getSession().getId();
        String serverKey = RedisKey.USER_SEVER_ID + userId;
        Integer severId = (Integer)redisTemplate.opsForValue().get(serverKey);
        if(severId == null){
            throw new GlobalException("用户未建立连接");
        }
        List<GroupMember> members = groupMemberService.findByUserId(userId);
        for(GroupMember member:members){
            String key = RedisKey.GROUP_ALREADY_READ_POSITION + member.getGroupId() + ":" + userId;
            Integer maxReadId = (Integer)redisTemplate.opsForValue().get(key);
            QueryWrapper<GroupMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .eq(GroupMessage::getGroupId,member.getGroupId())
                    .gt(GroupMessage::getSendId,member.getCreateTime())
                    .ne(GroupMessage::getSendId,userId)
                    .ne(GroupMessage::getStatus,MessageStatus.RECALL.code());
            if(maxReadId != null){
                queryWrapper.lambda().gt(GroupMessage::getId,maxReadId);
            }
            queryWrapper.last("limit 10");
            List<GroupMessage> list = this.list(queryWrapper);
            if(list.isEmpty()) continue;
            List<GroupMessageInfo> collect = list.stream().map(groupMessage -> {
                GroupMessageInfo groupMessageInfo = BeanUtil.copyProperties(groupMessage, GroupMessageInfo.class);
                return groupMessageInfo;
            }).collect(Collectors.toList());
            GroupMessageInfo[] array = collect.toArray(new GroupMessageInfo[collect.size()]);
            sender.sendGroupMessage(Collections.singletonList(userId),array);

        }
    }
}
