package com.yjc.server.processors;

import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.*;
import com.yjc.server.utils.CtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GroupMessageProcessor extends AbstractProcessor<ReceiveInfo<GroupMessageInfo>> {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(ReceiveInfo<GroupMessageInfo> receiveInfo){
        GroupMessageInfo data = receiveInfo.getData();
        List<Long> ids = receiveInfo.getReceiveIds();
        for(Long id:ids){
            try{
                ChannelHandlerContext context = CtxMap.get(id);
                if(context != null){
                    SendInfo<GroupMessageInfo> sendInfo = new SendInfo<>();
                    sendInfo.setCmd(CommandType.GROUP_MESSAGE.getCode());
                    sendInfo.setData(data);
                    context.channel().writeAndFlush(sendInfo);

                    String key = RedisKey.RESULT_GROUP_QUEUE;
                    ResultInfo<GroupMessageInfo> resultInfo = new ResultInfo<>();
                    resultInfo.setCode(SendCode.SUCCESS);
                    resultInfo.setReceiveId(id);
                    resultInfo.setMessageInfo(data);
                    redisTemplate.opsForList().rightPush(key,resultInfo);
                }
                else {
                    String key = RedisKey.RESULT_GROUP_QUEUE;
                    ResultInfo<GroupMessageInfo> resultInfo = new ResultInfo<>();
                    resultInfo.setCode(SendCode.NOT_FIND_CHANNEL);
                    resultInfo.setReceiveId(id);
                    resultInfo.setMessageInfo(data);
                    redisTemplate.opsForList().rightPush(key,resultInfo);
                }
            }
            catch (Exception e){
                String key = RedisKey.RESULT_GROUP_QUEUE;
                ResultInfo<GroupMessageInfo> resultInfo = new ResultInfo<>();
                resultInfo.setCode(SendCode.UNKNOWN_ERROR);
                resultInfo.setReceiveId(id);
                resultInfo.setMessageInfo(data);
                redisTemplate.opsForList().rightPush(key,resultInfo);
            }
        }
    }
}
