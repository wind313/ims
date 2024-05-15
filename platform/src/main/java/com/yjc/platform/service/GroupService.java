package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import com.yjc.platform.vo.InviteVO;

import java.util.List;

public interface GroupService extends IService<Group> {
    GroupVO create(String name);

    void update(GroupVO groupVO);

    void delete(Long id);

    GroupVO findById(Long id);

    List<GroupMemberVO> findMemberByGroupId(Long id);

    void quit(Long id);

    void kick(Long groupId, Long userId);

    List<GroupVO> getList();

    void invite(InviteVO inviteVO);

    void chatroom();
}
