package com.yjc.client;

import com.yjc.client.listener.MessageListenerMulticaster;
import com.yjc.common.constant.RedisKey;
import com.yjc.common.enums.CommandType;
import com.yjc.common.enums.ListenerType;
import com.yjc.common.enums.SendCode;
import com.yjc.common.model.*;
import lombok.extern.slf4j.Slf4j;
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
            ReceiveInfo[] receiveInfos = new ReceiveInfo[groupMessageInfos.length];
            for(int i=0;i<groupMessageInfos.length;i++){
                ReceiveInfo<GroupMessageInfo> receiveInfo = new ReceiveInfo<>();
                receiveInfo.setCmd(CommandType.GROUP_MESSAGE.getCode());
                receiveInfo.setReceiveIds(new LinkedList<>(entry.getValue()));
                receiveInfo.setData(groupMessageInfos[i]);
                receiveInfos[i] = receiveInfo;
            }
            String key = RedisKey.UNREAD_GROUP_QUEUE + entry.getKey();
            redisTemplate.opsForList().rightPushAll(key,receiveInfos);
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
