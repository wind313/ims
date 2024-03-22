package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.mapper.FriendMapper;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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

    @Override
    public boolean isFriend(Long id1, Long id2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Friend::getUserId,id1)
                .eq(Friend::getFriendId,id2);
        return count(queryWrapper)>0;
    }

    public void bind(Long userId, Long friendId) {
        if(!isFriend(userId,friendId)){
            Friend friend = new Friend();
            friend.setUserId(userId);
            friend.setFriendId(friendId);
            User friendIn = userService.findById(friendId);
            friend.setRemark(friendIn.getNickname());
            save(friend);
        }
        else{
            throw new GlobalException("对方已是你的好友");
        }
    }


    @Override
    public List<Friend> friends(Long id) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Friend::getUserId,id);
        return list(queryWrapper);
    }

    @Override
    public void delete(Long friendId) {
        Long userId = SessionContext.getSession().getId();
        unbind(userId,friendId);
        unbind(friendId,userId);
    }

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
}
