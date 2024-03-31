package com.yjc.server.websocket.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjc.common.model.SendInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Encoder extends MessageToMessageEncoder<SendInfo> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, SendInfo sendInfo, List<Object> list) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(sendInfo);

        TextWebSocketFrame frame = new TextWebSocketFrame(s);
        list.add(frame);
    }
}
