package com.yjc.client;

import com.yjc.client.listener.MessageListenerMulticaster;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Sender{

    @Autowired
    @Qualifier("IMRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageListenerMulticaster messageListenerMulticaster;

    public void sendPrivateMessage(Long receiveId, PrivateMessageInfo... privateMessageInfos){
        String key = RedisKey.USER_SEVER_ID + receiveId;
        Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
        if(serverId != null){
            String sendKey = RedisKey.UNREAD_PRIVATE_QUEUE + serverId;
            ReceiveInfo[] receiveInfos = new ReceiveInfo[privateMessageInfos.length];
            for(int i=0;i<privateMessageInfos.length;i++){
                ReceiveInfo<PrivateMessageInfo> receiveInfo = new ReceiveInfo<>();
                receiveInfo.setCmd(CommandType.PRIVATE_MESSAGE.getCode());
                receiveInfo.setData(privateMessageInfos[i]);
                LinkedList<Long> ids = new LinkedList<>();
                ids.add(receiveId);
                receiveInfo.setReceiveIds(ids);
                receiveInfos[i] = receiveInfo;
            }
            redisTemplate.opsForList().rightPushAll(sendKey,receiveInfos);
        }
        else {
            for(PrivateMessageInfo privateMessageInfo:privateMessageInfos){
                ResultInfo<PrivateMessageInfo> resultInfo = new ResultInfo<>();
                resultInfo.setReceiveId(receiveId);
                resultInfo.setCode(SendCode.NOT_ONLINE);
                resultInfo.setMessageInfo(privateMessageInfo);
                messageListenerMulticaster.multicast(ListenerType.PRIVATE,resultInfo);
            }

        }
    }

    public void sendGroupMessage(List<Long> receiveIds, GroupMessageInfo... groupMessageInfos){

    }
}
