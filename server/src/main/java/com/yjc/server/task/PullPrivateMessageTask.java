package com.yjc.server.task;

import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.model.ReceiveInfo;
import com.yjc.server.processors.AbstractProcessor;
import com.yjc.server.processors.ProcessorFactory;
import com.yjc.server.websocket.ServerGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PullPrivateMessageTask extends AbstractPullMessageTask {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void pullMessage() {
        String key = RedisKey.UNREAD_PRIVATE_QUEUE + ServerGroup.serverId;
        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
        for(Object o:range){
            ReceiveInfo receiveInfo = (ReceiveInfo) o;
            redisTemplate.opsForList().leftPop(key);
            AbstractProcessor processor = ProcessorFactory.createProcessor(CommandType.PRIVATE_MESSAGE);
            processor.process(receiveInfo);
        }
    }
}
