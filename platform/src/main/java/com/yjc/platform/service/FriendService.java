package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.vo.FriendVO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface FriendService extends IService<Friend> {
    void add(Long friendId);
    boolean isFriend(Long id1,Long id2);

    List<FriendVO> concerns();

    boolean isConcern(Long userId1, Long userId2);

    boolean isFans(Long userId1, Long userId2);

    void delete(Long friendId);

    List<Friend> findConcernsByUserId(Long id);

    List<Friend> findFansByUserId(Long id);

    List<Friend> findFriendByUserId(Long id);

    FriendVO findByFriendId(Long friendId);

    List<FriendVO> fans();

    List<FriendVO> friends();
}
