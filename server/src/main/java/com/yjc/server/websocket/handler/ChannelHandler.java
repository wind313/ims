package com.yjc.server.websocket.handler;

import com.yjc.common.enums.CommandType;
import com.yjc.common.model.SendInfo;
import com.yjc.server.processors.FatherProcessor;
import com.yjc.server.processors.ProcessorFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChannelHandler extends SimpleChannelInboundHandler<SendInfo> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, SendInfo sendInfo) throws Exception {
        FatherProcessor processor = ProcessorFactory.createProcessor(CommandType.fromCode(sendInfo.getCmd()));
        processor.process(channelHandlerContext,processor.tranForm(sendInfo.getData()));
    }
}
