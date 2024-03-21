package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.GroupMember;

import java.util.List;

public interface GroupMemberService extends IService<GroupMember> {

    List<GroupMember> findByUserId(Long id);

    List<Long> findMemberIdsByGroupId(Long id);

    List<GroupMember> findByGroupId(Long id);
}
