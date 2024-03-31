package com.yjc.server.processors;

import cn.hutool.core.bean.BeanUtil;
import com.yjc.common.constant.Constant;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.model.HeartBeatInfo;
import com.yjc.common.model.LoginInfo;
import com.yjc.common.model.SendInfo;
import com.yjc.server.websocket.server.IMServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class HeartBeatProcessor extends FatherProcessor<HeartBeatInfo> {

    @Autowired
    IMServer server;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, HeartBeatInfo data){
        SendInfo sendInfo = new SendInfo();
        sendInfo.setCmd(CommandType.HEART_BEAT.getCode());
        ctx.channel().writeAndFlush(sendInfo);

        AttributeKey<Long> attr = AttributeKey.valueOf("HEARTBEAT_TIMES");
        Long heartbeatTimes = ctx.channel().attr(attr).get();
        ctx.channel().attr(attr).set(++heartbeatTimes);

        if(heartbeatTimes%10 == 0){
            attr = AttributeKey.valueOf("USER_ID");
            Long userId = ctx.channel().attr(attr).get();
            String key = RedisKey.USER_SEVER_ID+userId;
            redisTemplate.expire(key, Constant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }

    }

    @Override
    public HeartBeatInfo transform(Object o){
        HashMap map = (HashMap)o;
        HeartBeatInfo heartBeatInfo = BeanUtil.fillBeanWithMap(map, new HeartBeatInfo(), false);
        return heartBeatInfo;
    }
}
