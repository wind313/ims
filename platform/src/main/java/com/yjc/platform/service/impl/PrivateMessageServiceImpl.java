package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.client.Sender;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.constants.Constant;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.mapper.PrivateMessageMapper;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.PrivateMessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

    @Autowired
    private FriendService friendService;

    @Autowired
    private Sender sender;

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public Long send(PrivateMessageVO privateMessageVO) {
        Long userId = SessionContext.getSession().getId();
        if(!friendService.isFriend(userId,privateMessageVO.getReceiveId())){
            throw new GlobalException("对方不是你的好友");
        }
        PrivateMessage privateMessage = BeanUtil.copyProperties(privateMessageVO, PrivateMessage.class);
        privateMessage.setSendId(userId);
        privateMessage.setStatus(MessageStatus.UNREAD.code());
        privateMessage.setSendTime(new Date());
        save(privateMessage);

        PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(privateMessage, PrivateMessageInfo.class);
        sender.sendPrivateMessage(privateMessageVO.getReceiveId(),privateMessageInfo);

        return privateMessage.getId();
    }

    @Override
    public void recall(Long id) {
        PrivateMessage message = getById(id);
        Long userId = SessionContext.getSession().getId();
        if(message == null){
            throw new GlobalException("消息不存在");
        }
        if(!message.getSendId().equals(userId)){
            throw new GlobalException("你不是发送者");
        }
        if(System.currentTimeMillis()-message.getSendTime().getTime()> Constant.ALLOW_RECALL_TIME){
            throw new GlobalException("已超过两分钟，无法撤回");
        }
        message.setStatus(MessageStatus.RECALL.code());
        updateById(message);

        PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(message,PrivateMessageInfo.class);
        privateMessageInfo.setType(MessageType.TIP.getCode());
        privateMessageInfo.setContent("对方撤回了一条消息");
        privateMessageInfo.setSendTime(new Date());
        sender.sendPrivateMessage(message.getReceiveId(),privateMessageInfo);

    }

    @Override
    public void pullUnreadMessage() {
        Long userId = SessionContext.getSession().getId();
        String key = RedisKey.USER_SEVER_ID + userId;
        Integer severId = (Integer)redisTemplate.opsForValue().get(key);
        if(severId == null){
            throw new GlobalException("用户未建立连接");
        }
        QueryWrapper<PrivateMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(PrivateMessage::getReceiveId,userId)
                .eq(PrivateMessage::getStatus,MessageStatus.UNREAD);
        List<PrivateMessage> list = this.list(queryWrapper);

        if(!list.isEmpty()){
            List<PrivateMessageInfo> collect = list.stream().map(privateMessage -> {
                PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(privateMessage, PrivateMessageInfo.class);
                return privateMessageInfo;
            }).collect(Collectors.toList());
            PrivateMessageInfo[] arr = collect.toArray(new PrivateMessageInfo[list.size()]);
            sender.sendPrivateMessage(userId,arr);
        }

    }

    @Override
    public List<PrivateMessageInfo> history(Long friendId, Long page, Long size) {
        page = page>0?page:1;
        size = size>0?size:10;
        Long userId = SessionContext.getSession().getId();
        long idx = (page - 1) * size;
        QueryWrapper<PrivateMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .and(qWrapper->{
                    qWrapper.and(wrapper->wrapper.eq(PrivateMessage::getSendId,userId)
                                .eq(PrivateMessage::getReceiveId,friendId))
                            .or(
                                    wrapper->wrapper.eq(PrivateMessage::getReceiveId,userId)
                                            .eq(PrivateMessage::getSendId,friendId)
                            );
                })
                .ne(PrivateMessage::getStatus,MessageStatus.RECALL.code())
                .orderByDesc(PrivateMessage::getId)
                .last("limit " + idx + "," + size);
        List<PrivateMessage> list = this.list(queryWrapper);

        List<PrivateMessageInfo> collect = list.stream().map(message -> {
            PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(message, PrivateMessageInfo.class);
            return privateMessageInfo;

        }).collect(Collectors.toList());

        return collect;
    }


}
