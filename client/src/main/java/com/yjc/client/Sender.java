package com.yjc.client;

import com.yjc.client.listener.MessageListenerMulticaster;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Sender{

    @Autowired
    @Qualifier("IMRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MessageListenerMulticaster messageListenerMulticaster;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String exchange="yjc-im";
    private static final String PRIVATE_ROUTING_KEY="private";
    private static final String GROUP_ROUTING_KEY="group";

    public void sendPrivateMessage(PrivateMessageInfo privateMessageInfo){
        String key = RedisKey.USER_SEVER_ID + privateMessageInfo.getReceiveId();
        Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
        if(serverId != null){
            String sendKey = PRIVATE_ROUTING_KEY + serverId;
            log.info(sendKey);
            ReceiveInfo<PrivateMessageInfo> receiveInfo = new ReceiveInfo<>();
            receiveInfo.setCmd(CommandType.PRIVATE_MESSAGE.getCode());
            receiveInfo.setData(privateMessageInfo);
            LinkedList<Long> ids = new LinkedList<>();
            ids.add(privateMessageInfo.getReceiveId());
            receiveInfo.setReceiveIds(ids);
            rabbitTemplate.convertAndSend(exchange+serverId,sendKey,receiveInfo);
        }
        else {
            ResultInfo<PrivateMessageInfo> resultInfo = new ResultInfo<>();
            resultInfo.setReceiveId(privateMessageInfo.getReceiveId());
            resultInfo.setCode(SendCode.NOT_ONLINE);
            resultInfo.setMessageInfo(privateMessageInfo);
            messageListenerMulticaster.multicast(ListenerType.PRIVATE,resultInfo);
        }
    }

    public void sendGroupMessage(List<Long> receiveIds, GroupMessageInfo... groupMessageInfos){
        List<Long> offLineIds = Collections.synchronizedList(new LinkedList<Long>());
        Map<Integer,List<Long>> map = new ConcurrentHashMap<>();
        receiveIds.parallelStream().forEach(id->{
            String key = RedisKey.USER_SEVER_ID + id;
            Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
            if(serverId != null){
                synchronized (map){
                    if(map.containsKey(serverId)){
                        map.get(serverId).add(id);
                    }
                    else {
                        List<Long> list = Collections.synchronizedList(new LinkedList<Long>());
                        list.add(id);
                        map.put(serverId,list);
                    }
                }
            }
            else {
                offLineIds.add(id);
            }
        });
        for(Map.Entry<Integer,List<Long>> entry:map.entrySet()){
            String sendKey = GROUP_ROUTING_KEY + entry.getKey();
            String exchangeName = exchange+entry.getKey();
            for(int i=0;i<groupMessageInfos.length;i++){
                ReceiveInfo<GroupMessageInfo> receiveInfo = new ReceiveInfo<>();
                receiveInfo.setCmd(CommandType.GROUP_MESSAGE.getCode());
                receiveInfo.setReceiveIds(new LinkedList<>(entry.getValue()));
                receiveInfo.setData(groupMessageInfos[i]);
                rabbitTemplate.convertAndSend(exchangeName, sendKey,receiveInfo);
            }
        }
        for(GroupMessageInfo groupMessageInfo:groupMessageInfos){
            for(Long id:offLineIds){
                ResultInfo<GroupMessageInfo> resultInfo = new ResultInfo<>();
                resultInfo.setCode(SendCode.NOT_ONLINE);
                resultInfo.setMessageInfo(groupMessageInfo);
                resultInfo.setReceiveId(id);
                messageListenerMulticaster.multicast(ListenerType.GROUP,resultInfo);
            }

        }
    }
}
