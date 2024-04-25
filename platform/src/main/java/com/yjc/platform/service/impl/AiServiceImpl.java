package com.yjc.platform.service.impl;

import com.yjc.common.model.PrivateMessageInfo;
import com.yjc.platform.constants.RedisKey;
import com.yjc.platform.enums.MessageStatus;
import com.yjc.platform.enums.MessageType;
import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.pojo.PrivateMessage;
import com.yjc.platform.service.AiService;
import com.yjc.platform.service.PrivateMessageService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Autowired
    private OllamaChatClient aiClient;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    @Transactional
    public String sendAndResponse(Long AiId, String message){
        Long userId = SessionContext.getSession().getId();
        send(userId,AiId,message);
        PrivateMessageInfo response = response(userId,AiId, message);
        return response.getContent();
    }
    @Override
    public PrivateMessageInfo send(Long userId,Long AiId, String message){

        String key = RedisKey.IM_FRIEND+"::"+AiId+":"+userId;
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        if(count == null ){
            redisTemplate.opsForValue().set(key,0);
            LocalDate today = LocalDate.now();
            LocalDateTime tomorrow = today.plusDays(1).atStartOfDay();
            long seconds = LocalDateTime.now().until(tomorrow, ChronoUnit.SECONDS);
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        else if(count >= 5){
            throw new GlobalException("发送消息已达上限");
        }
        else if(count >= 0) redisTemplate.opsForValue().increment(key,1);

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSendTime(new Date());
        privateMessage.setSendId(userId);
        privateMessage.setStatus(MessageStatus.ALREADY_READ.code());
        privateMessage.setType(MessageType.TEXT.getCode());
        privateMessage.setContent(message);
        privateMessage.setReceiveId(AiId);
        privateMessageService.save(privateMessage);
        PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(privateMessage, PrivateMessageInfo.class);
        return privateMessageInfo;
    }

    @Override
    public PrivateMessageInfo response(Long userId,Long AiId, String message) {
        String response = "";
        try{
            response = aiClient.call(message);
        }catch (Exception e){
            throw new GlobalException(e.getMessage());
        }
        PrivateMessage resp = new PrivateMessage();
        resp.setSendTime(new Date());
        resp.setSendId(AiId);
        resp.setStatus(MessageStatus.ALREADY_READ.code());
        resp.setType(MessageType.TEXT.getCode());
        resp.setContent(response);
        resp.setReceiveId(userId);
        privateMessageService.save(resp);
        PrivateMessageInfo privateMessageInfo = BeanUtil.copyProperties(resp, PrivateMessageInfo.class);
        return privateMessageInfo;
    }

    @Override
    public List<PrivateMessageInfo> history(Long userId,Long AiId,Long page, Long size) {
        return privateMessageService.history(userId,AiId,page,size);
    }
}