package com.yjc.client.task;

import com.yjc.client.listener.MessageListenerMulticaster;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.model.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class PullPrivateResultTask extends AbstractPullResultTask{

    @Autowired
    @Qualifier("IMRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageListenerMulticaster listenerMulticaster;

    @Override
    public void pullMessage() {
        String key = RedisKey.RESULT_PRIVATE_QUEUE;
        ResultInfo result = (ResultInfo)redisTemplate.opsForList().leftPop(key, 10, TimeUnit.SECONDS);
        if(result != null){
            listenerMulticaster.multicast(ListenerType.PRIVATE,result);
        }
    }
}
