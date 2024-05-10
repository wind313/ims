package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.constants.RedisKey;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.mapper.FriendMapper;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.FriendVO;
import com.yjc.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CacheConfig(cacheNames = RedisKey.IM_FRIEND)
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void add(Long friendId) {
        long userId = SessionContext.getSession().getId();
        if (userId == friendId) {
            throw new GlobalException("不能关注自己");
        }
        bind(userId, friendId);
    }

    @Override
    public boolean isConcern(Long userId1, Long userId2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId1)
                .eq(Friend::getFriendId, userId2);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean isFans(Long userId1, Long userId2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId2)
                .eq(Friend::getFriendId, userId1);
        return count(queryWrapper) > 0;
    }

    @Override
    @Cacheable(key = "#p0+':'+#p1")
    public boolean isFriend(Long userId1, Long userId2) {

        return isConcern(userId1, userId2) && isFans(userId1, userId2);
    }

    public void bind(Long userId, Long friendId) {
        if (!isConcern(userId, friendId)) {
            Friend friend = new Friend();
            friend.setUserId(userId);
            friend.setFriendId(friendId);
            save(friend);
            redisTemplate.delete(RedisKey.IM_FRIEND + "::" + userId + ":" + friendId);
            redisTemplate.delete(RedisKey.IM_FRIEND + "::" + friendId + ":" + userId);
        } else {
            throw new GlobalException("你已关注对方");
        }
    }

    public List<FriendVO> getList(int type) {
        Long userId = SessionContext.getSession().getId();
        List<Friend> friends = findFriendByUserId(userId);
        ;
        List<Friend> list = null;
        if (type == 0) {
            list = findConcernsByUserId(userId);
        } else if (type == 1) {
            list = findFansByUserId(userId);
        } else if (type == 2) {
            list = friends;
        }

        if (list == null || list.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<Long> ids = null;
        if(type == 0 || type == 2){
            ids = list.stream().map(friend -> friend.getFriendId()).collect(Collectors.toList());
        }
        else if(type == 1){
            ids = list.stream().map(friend -> friend.getUserId()).collect(Collectors.toList());
        }

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(User::getId, ids);
        List<User> users = userService.list(wrapper);

        List<FriendVO> l = list.stream().map(friend -> {
            FriendVO friendVO = new FriendVO();
            User user1 = null;
            if (type == 0){
                user1 = users.stream().filter(user -> user.getId() == friend.getFriendId()).findFirst().get();
            }
            else if (type == 1){
                user1 = users.stream().filter(user -> user.getId() == friend.getUserId()).findFirst().get();
            }
            else if (type == 2) {
                user1 = users.stream().filter(user -> user.getId() == friend.getFriendId()).findFirst().get();
            }
            friendVO.setFriendId(user1.getId());
            friendVO.setNickname(user1.getNickname());
            friendVO.setHeadImage(user1.getHeadImageThumb());
            friendVO.setSignature(user1.getSignature());
            if (type == 0) {
                friendVO.setIsConcern(true);
                if (friends.stream().filter(f -> f.getFriendId() == friend.getFriendId()).count()>0L)
                    friendVO.setIsFans(true);
                else friendVO.setIsFans(false);
            } else if (type == 1) {
                friendVO.setIsFans(true);
                if (friends.stream().filter(f -> f.getFriendId() == friend.getUserId()).count()>0L)
                    friendVO.setIsConcern(true);
                else friendVO.setIsConcern(false);
            } else if (type == 2) {
                friendVO.setIsConcern(true);
                friendVO.setIsFans(true);
            }


            return friendVO;
        }).collect(Collectors.toList());

        return l;
    }

    @Override
    public void delete(Long friendId) {
        Long userId = SessionContext.getSession().getId();
        unbind(userId, friendId);
    }

    public void unbind(Long userId, Long friendId) {
        log.info(userId.toString(), friendId);
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId);
        Friend one = getOne(queryWrapper);
        if (one == null) {
            throw new GlobalException("对方不是你的好友");
        }
        removeById(one.getId());
        redisTemplate.delete(RedisKey.IM_FRIEND + "::" + userId + ":" + friendId);
        redisTemplate.delete(RedisKey.IM_FRIEND + "::" + friendId + ":" + userId);
    }

    @Override
    public List<Friend> findConcernsByUserId(Long id) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Friend::getUserId, id);

        return list(queryWrapper);
    }

    @Override
    public List<Friend> findFansByUserId(Long id) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Friend::getFriendId, id);
        return list(queryWrapper);
    }

    @Override
    public List<Friend> findFriendByUserId(Long id) {
        List<Friend> concerns = findConcernsByUserId(id);
        List<Friend> fans = findFansByUserId(id);
        List<Friend> result = concerns.stream()
                .filter(concern -> fans.stream()
                        .anyMatch(fan -> concern.getUserId().equals(fan.getFriendId()) && concern.getFriendId().equals(fan.getUserId()))
                )
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public FriendVO findByFriendId(Long friendId) {
        Long userId = SessionContext.getSession().getId();
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Friend::getFriendId, friendId)
                .eq(Friend::getUserId, userId);
        Friend friend = getOne(queryWrapper);
        if (friend == null) {
            throw new GlobalException("对方不是你的好友");
        }
        UserVO user = userService.findById(friendId);
        FriendVO friendVO = new FriendVO();
        friendVO.setFriendId(friendId);
        friendVO.setNickname(user.getNickname());
        friendVO.setHeadImage(user.getHeadImageThumb());
        friendVO.setSignature(user.getSignature());
        friendVO.setIsFans(user.getIsFans());
        friendVO.setIsConcern(user.getIsConcern());
        return friendVO;
    }

    @Override
    public List<FriendVO> concerns() {
        return getList(0);
    }

    @Override
    public List<FriendVO> fans() {
        return getList(1);
    }

    @Override
    public List<FriendVO> friends() {
        return getList(2);
    }


}
