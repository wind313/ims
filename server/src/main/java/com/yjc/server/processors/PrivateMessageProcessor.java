package com.yjc.server.processors;

import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.*;
import com.yjc.server.utils.CtxMap;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PrivateMessageProcessor extends AbstractProcessor<ReceiveInfo<PrivateMessageInfo>> {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    @Override
    public void process( ReceiveInfo<PrivateMessageInfo> receiveInfo){
        PrivateMessageInfo data = receiveInfo.getData();
        Long receiveId = receiveInfo.getReceiveIds().get(0);
        try{
            ChannelHandlerContext context = CtxMap.get(receiveId);
            if(context != null){
                SendInfo<PrivateMessageInfo> sendInfo = new SendInfo<>();
                sendInfo.setCmd(CommandType.PRIVATE_MESSAGE.getCode());
                sendInfo.setData(data);
                context.channel().writeAndFlush(sendInfo);

                String key = RedisKey.RESULT_PRIVATE_QUEUE;
                ResultInfo<PrivateMessageInfo> resultInfo = new ResultInfo<>();
                resultInfo.setCode(SendCode.SUCCESS);
                resultInfo.setReceiveId(receiveId);
                resultInfo.setMessageInfo(data);
                redisTemplate.opsForList().rightPush(key,resultInfo);
            }
            else {
                String key = RedisKey.RESULT_PRIVATE_QUEUE;
                ResultInfo<PrivateMessageInfo> resultInfo = new ResultInfo<>();
                resultInfo.setCode(SendCode.NOT_FIND_CHANNEL);
                resultInfo.setReceiveId(receiveId);
                resultInfo.setMessageInfo(data);
                redisTemplate.opsForList().rightPush(key,resultInfo);
            }
        }
        catch (Exception e){
            String key = RedisKey.RESULT_PRIVATE_QUEUE;
            ResultInfo<PrivateMessageInfo> resultInfo = new ResultInfo<>();
            resultInfo.setCode(SendCode.UNKNOWN_ERROR);
            resultInfo.setReceiveId(receiveId);
            resultInfo.setMessageInfo(data);
            redisTemplate.opsForList().rightPush(key,resultInfo);
        }


    }




}
