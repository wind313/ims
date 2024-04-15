package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.GroupMember;

import java.util.List;

public interface GroupMemberService extends IService<GroupMember> {

    void removeMember(Long groupId, Long userId);

    List<GroupMember> findByUserId(Long id);

    List<Long> findMemberIdsByGroupId(Long id);

    List<GroupMember> findByGroupId(Long id);

    GroupMember findByGroupIdAndUserId(Long groupId, Long userId);

    void deleteByGroupId(Long id);

    boolean saveOrUpdateBatch(Long id, List<GroupMember> collect);


}
