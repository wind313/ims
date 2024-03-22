package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Constants.Constant;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.mapper.GroupMessageMapper;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMessage;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.vo.GroupMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements GroupMessageService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMemberService groupMemberService;
    @Override
    public Long send(GroupMessageVO groupMessageVO) {
        Long userId = SessionContext.getSession().getId();
        Group group= groupService.getById(groupMessageVO.getReceiveId());
        if(group == null){
            throw new GlobalException("群聊不存在");
        }
        if (group.getDeleted()){
            throw new GlobalException("群聊已解散");
        }

        List<Long> list = groupMemberService.findMemberIdsByGroupId(groupMessageVO.getReceiveId());
        if(!list.contains(userId)){
            throw new GlobalException("你不在群聊中");
        }
        GroupMessage groupMessage = BeanUtils.copyProperties(groupMessageVO, GroupMessage.class);
        groupMessage.setSendId(userId);
        save(groupMessage);

        list = list.stream().filter(id -> id != userId).collect(Collectors.toList());


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

        List<Long> list = groupMemberService.findMemberIdsByGroupId(groupMessage.getReceiveId());
        if(!list.contains(userId)){
            throw new GlobalException("你不在群聊中");
        }

        groupMessage.setStatus(MessageStatus.RECALL.code());
        updateById(groupMessage);

        list.stream().filter(uid -> id!=userId).collect(Collectors.toList());


    }
}
