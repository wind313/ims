package com.yjc.server.processors;

import cn.hutool.core.bean.BeanUtil;
import com.yjc.common.constant.Constant;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.model.LoginInfo;
import com.yjc.common.model.SendInfo;
import com.yjc.server.utils.CtxMap;
import com.yjc.server.websocket.ServerGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginProcessor extends FatherProcessor<LoginInfo> {

    @Autowired
    RedisTemplate<String,Object> template;

    @Override
    public void process(ChannelHandlerContext ctx, LoginInfo data){
        ChannelHandlerContext context = CtxMap.get(data.getUserId());
        if(context != null){
            SendInfo sendInfo = new SendInfo();
            sendInfo.setCmd(CommandType.LOGOUT.getCode());
            context.channel().writeAndFlush(sendInfo);
        }
        CtxMap.add(data.getUserId(),ctx);
        AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
        ctx.channel().attr(attr).set(data.getUserId());
        attr = AttributeKey.valueOf("HEARTBEAT_TIMES");
        ctx.channel().attr(attr).set(0L);
        String key = RedisKey.USER_SEVER_ID+data.getUserId();
        template.opsForValue().set(key, ServerGroup.serverId, Constant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        SendInfo sendInfo = new SendInfo();
        sendInfo.setCmd(CommandType.LOGIN.getCode());
        ctx.channel().writeAndFlush(sendInfo);
    }

    @Override
    public LoginInfo transform(Object o){
        HashMap map = (HashMap)o;
        LoginInfo loginInfo = BeanUtil.fillBeanWithMap(map, new LoginInfo(), false);
        return loginInfo;
    }

}
