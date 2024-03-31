package com.yjc.platform.listener;

import com.yjc.client.annotation.Listener;
import com.yjc.client.listener.MessageListener;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.GroupMessageInfo;
import com.yjc.common.model.ResultInfo;
import com.yjc.platform.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Listener(type = ListenerType.GROUP)
public class GroupMessageListener implements MessageListener {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Override
    public void process(ResultInfo resultInfo) {
        GroupMessageInfo messageInfo = (GroupMessageInfo) resultInfo.getMessageInfo();
        if(messageInfo.getType().equals(MessageType.TIP.getCode())){
            return;
        }

        if(resultInfo.getCode().equals(SendCode.SUCCESS)){
            String key = RedisKey.GROUP_ALREADY_READ_POSITION + messageInfo.getReceiveId() + ":" + resultInfo.getReceiveId();
            redisTemplate.opsForValue().set(key,messageInfo.getId());
        }
    }
}
