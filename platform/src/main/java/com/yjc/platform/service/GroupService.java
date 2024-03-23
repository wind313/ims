package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupMessageVO;
import com.yjc.platform.vo.GroupVO;

import java.util.List;

public interface GroupService extends IService<Group> {
    GroupVO create(String name);

    void update(GroupVO groupVO);

    void delete(Long id);

    GroupVO findById(Long id);

    List<GroupMemberVO> findMemberByGroupId(Long id);
}
