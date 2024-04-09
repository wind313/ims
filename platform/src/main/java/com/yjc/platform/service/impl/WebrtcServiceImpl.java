package com.yjc.platform.service.impl;

import com.yjc.client.Sender;
import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.config.ICEServerConfig;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.service.WebrtcService;
import com.yjc.platform.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebrtcServiceImpl implements WebrtcService {

    @Autowired
    private Sender sender;

    @Autowired
    private ICEServerConfig iceServerConfig;


    @Override
    public void call(Long uid, String offer) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_CALL.getCode());
        privateMessageInfo.setContent(offer);
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public void accept(Long uid, String answer) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_ACCEPT.getCode());
        privateMessageInfo.setContent(answer);
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public void reject(Long uid) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_REJECT.getCode());
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);

    }

    @Override
    public void cancel(Long uid) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_CANCEL.getCode());
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public void failed(Long uid, String reason) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_FAILED.getCode());
        privateMessageInfo.setContent(reason);
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public void handup(Long uid) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_HANGUP.getCode());
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public void candidate(Long uid, String candidate) {
        Long userId = SessionContext.getSession().getId();
        PrivateMessageInfo privateMessageInfo = new PrivateMessageInfo();
        privateMessageInfo.setType(MessageType.RTC_CANDIDATE.getCode());
        privateMessageInfo.setContent(candidate);
        privateMessageInfo.setReceiveId(uid);
        privateMessageInfo.setSendId(userId);
        sender.sendPrivateMessage(uid,privateMessageInfo);
    }

    @Override
    public Object iceservers() {
        return iceServerConfig.getIceServers();
    }
}
