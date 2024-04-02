package com.yjc.server.websocket.handler;

import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.model.SendInfo;
import com.yjc.server.processors.AbstractProcessor;
import com.yjc.server.processors.ProcessorFactory;
import com.yjc.server.utils.CtxMap;
import com.yjc.server.utils.SpringContextHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class ChannelHandler extends SimpleChannelInboundHandler<SendInfo> {

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, SendInfo sendInfo) throws Exception {
        AbstractProcessor processor = ProcessorFactory.createProcessor(CommandType.fromCode(sendInfo.getCmd()));
        processor.process(channelHandlerContext,processor.transform(sendInfo.getData()));
    }
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        log.info(context.channel().id().asLongText()+"连接");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.READER_IDLE){
                AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
                Long userId = ctx.channel().attr(attr).get();
                log.info("心跳超时，用户：{}",userId);
                ctx.channel().close();
            }
        }
        else ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
        Long userId = ctx.channel().attr(attr).get();
        ChannelHandlerContext context = CtxMap.get(userId);
        if(context != null && ctx.channel().id().equals(context.channel().id())){
            CtxMap.remove(userId);
            RedisTemplate redisTemplate = (RedisTemplate) SpringContextHolder.getApplicationContext().getBean("redisTemplate");
            String key =RedisKey.USER_SEVER_ID+ userId;
            redisTemplate.delete(key);
            log.info("{}断开连接",userId);
        }
    }



}
