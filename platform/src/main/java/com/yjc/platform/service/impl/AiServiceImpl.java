package com.yjc.platform.service.impl;

import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.constants.AiConstant;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.AiService;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.session.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.client.AiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Autowired
    private AiClient aiClient;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Override
    @Transactional
    public String sendAndResponse(Long AiId, String message){
        Long userId = SessionContext.getSession().getId();
        send(userId,AiId,message);
        PrivateMessage response = response(userId,AiId, message);
        return response.getContent();
    }
    @Override
    public PrivateMessage send(Long userId,Long AiId, String message){

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSendTime(new Date());
        privateMessage.setSendId(userId);
        privateMessage.setStatus(MessageStatus.ALREADY_READ.code());
        privateMessage.setType(MessageType.TEXT.getCode());
        privateMessage.setContent(message);
        privateMessage.setReceiveId(AiId);
        privateMessageService.save(privateMessage);
        return privateMessage;
    }

    @Override
    public PrivateMessage response(Long userId,Long AiId, String message) {
        StringBuilder messages = new StringBuilder();
        messages.append(AiConstant.PROMPT_WHAT_WERE_WE_TALKING_ABOUT);
        List<PrivateMessageInfo> history = history(userId,AiId,1L, 10L);
        for(PrivateMessageInfo privateMessageInfo:history){
            if(privateMessageInfo.getSendId().equals(userId) ){
                messages.append(AiConstant.PROMPT_DELIMITER + privateMessageInfo.getContent() + AiConstant.PROMPT_DELIMITER);
            }
        }
        messages.append(AiConstant.PROMPT_DELIMITER_FOR_HISTORICAL_CONTEXT);
        messages.append(AiConstant.PROMPT_USE_CONTEXT_IF_NEEDED);
        messages.append(AiConstant.PROMPT_THE_CURRENT_QUESTION+message);
        String response = "";
        try{
            response = aiClient.generate(messages.toString());
        }catch (Exception e){
            throw new GlobalException(ResultCode.PROGRAM_ERROR.getMessage());
        }
        PrivateMessage resp = new PrivateMessage();
        resp.setSendTime(new Date());
        resp.setSendId(AiId);
        resp.setStatus(MessageStatus.ALREADY_READ.code());
        resp.setType(MessageType.TEXT.getCode());
        resp.setContent(response);
        resp.setReceiveId(userId);
        privateMessageService.save(resp);
        return resp;
    }

    @Override
    public List<PrivateMessageInfo> history(Long userId,Long AiId,Long page, Long size) {
        return privateMessageService.history(userId,AiId,page,size);
    }
}