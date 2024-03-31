package com.yjc.platform.listener;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yjc.client.Sender;
import com.yjc.client.annotation.Listener;
import com.yjc.client.listener.MessageListener;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.common.model.ResultInfo;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.PrivateMessageService;
import org.springframework.beans.factory.annotation.Autowired;

@Listener(type = ListenerType.PRIVATE)
public class PrivateMessageListener implements MessageListener {

    @Autowired
    private Sender sender;

    @Autowired
    private PrivateMessageService privateMessageService;
    @Override
    public void process(ResultInfo resultInfo) {

         PrivateMessageInfo messageInfo = (PrivateMessageInfo)resultInfo.getMessageInfo();

         if(messageInfo.getType().equals(MessageType.TIP.getCode())){
             return;
         }

         if(messageInfo.getType() >= MessageType.RTC_CALL.getCode()
                 && messageInfo.getType()< MessageType.RTC_CANDIDATE.getCode()){
             if(messageInfo.getType().equals(MessageType.RTC_CALL.getCode())
                     && !resultInfo.getCode().equals(SendCode.SUCCESS)){
                 PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
                 privateMessageInfo.setType(MessageType.RTC_FAILED.getCode());
                 privateMessageInfo.setContent(MessageType.RTC_FAILED.getDesc());
                 privateMessageInfo.setSendId(messageInfo.getReceiveId());
                 privateMessageInfo.setReceiveId(messageInfo.getSendId());
                 sender.sendPrivateMessage(messageInfo.getSendId(),privateMessageInfo);
             }
         }

         if(resultInfo.getCode().equals(SendCode.SUCCESS)){
             UpdateWrapper<PrivateMessage> updateWrapper = new UpdateWrapper<>();
             updateWrapper.lambda().eq(PrivateMessage::getId,messageInfo.getId())
                     .eq(PrivateMessage::getStatus, MessageStatus.UNREAD.code())
                     .set(PrivateMessage::getStatus,MessageStatus.ALREADY_READ.code());
             privateMessageService.update(updateWrapper);
         }
    }
}
