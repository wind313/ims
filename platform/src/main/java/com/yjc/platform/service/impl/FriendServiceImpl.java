package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.mapper.FriendMapper;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.FriendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
//@CacheConfig(cacheNames = RedisKey.IM_FRIEND)
public class FriendServiceImpl extends ServiceImpl<FriendMapper,Friend> implements FriendService {

    @Autowired
    private UserService userService;


    @Transactional
    @Override
    public void add(Long friendId) {
        long userId = SessionContext.getSession().getId();
        if(userId == friendId){
            throw new GlobalException("不能加自己为好友");
        }
        bind(userId,friendId);
        bind(friendId,userId);
    }

//    @Cacheable(key = "#p0+':'+#p1")
    @Override
    public boolean isFriend(Long userId1, Long userId2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId,userId1)
                .eq(Friend::getFriendId,userId2);
        return count(queryWrapper)>0;
    }
//    @CacheEvict(key = "#p0+':'+#p1")
    public void bind(Long userId, Long friendId) {
        if(!isFriend(userId,friendId)){
            Friend friend = new Friend();
            friend.setUserId(userId);
            friend.setFriendId(friendId);
            save(friend);
        }
        else{
            throw new GlobalException("对方已是你的好友");
        }
    }


    @Override
    public List<FriendVO> friends() {
        Long userId = SessionContext.getSession().getId();

        List<Friend> list = findByUserId(userId);
        if(list == null || list.size() == 0){
            return Collections.EMPTY_LIST;
        }
        List<Long> ids = list.stream().map(friend -> friend.getFriendId()).collect(Collectors.toList());

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(User::getId,ids);
        List<User> users = userService.list(wrapper);


        List<FriendVO> l = list.stream().map(friend -> {
            FriendVO friendVO = BeanUtil.copyProperties(friend, FriendVO.class);
            User user1 = users.stream().filter(user -> user.getId() == friend.getFriendId()).findFirst().get();
            friendVO.setNickname(user1.getNickname());
            friendVO.setHeadImage(user1.getHeadImageThumb());
            return friendVO;
        }).collect(Collectors.toList());
        return l;
    }

    @Transactional
    @Override
    public void delete(Long friendId) {
        Long userId = SessionContext.getSession().getId();
        unbind(userId,friendId);
        unbind(friendId,userId);
    }
//    @CacheEvict(key = "#p0+':'+#p1")
    public void unbind(Long userId,Long friendId){
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId,userId)
                .eq(Friend::getFriendId,friendId);
        Friend one = getOne(queryWrapper);
        if(one == null ){
            throw new GlobalException("对方不是你的好友");
        }
        removeById(one.getId());
    }

    @Override
    public List<Friend> findByUserId(Long id){
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Friend::getUserId,id);
        return list(queryWrapper);
    }

}
