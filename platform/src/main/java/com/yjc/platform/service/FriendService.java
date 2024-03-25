package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.vo.FriendVO;

import java.util.List;

public interface FriendService extends IService<Friend> {
    void add(Long friendId);
    boolean isFriend(Long id1,Long id2);

    List<FriendVO> friends();

    void delete(Long friendId);

    List<Friend> findByUserId(Long id);

}
