package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Friend;

import java.util.List;

public interface FriendService extends IService<Friend> {
    void add(Long friendId);
    boolean isFriend(Long id1,Long id2);

    List<Friend> friends(Long id);

    void delete(Long friendId);
}
