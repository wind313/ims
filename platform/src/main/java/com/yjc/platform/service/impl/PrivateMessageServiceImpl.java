package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Constants.Constant;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.mapper.PrivateMessageMapper;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.vo.PrivateMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

    @Autowired
    private FriendService friendService;

    @Override
    public Long send(PrivateMessageVO privateMessageVO) {
        Long userId = SessionContext.getSession().getId();
        if(!friendService.isFriend(userId,privateMessageVO.getReceiveId())){
            throw new GlobalException("对方不是你的好友");
        }
        PrivateMessage privateMessage = BeanUtils.copyProperties(privateMessageVO, PrivateMessage.class);
        privateMessage.setSendId(userId);
        privateMessage.setStatus(MessageStatus.UNREAD.code());
        save(privateMessage);


        return null;
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



    }

}
