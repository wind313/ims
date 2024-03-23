package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.mapper.GroupMemberMapper;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.service.GroupMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {
    @Override
    public List<GroupMember> findByUserId(Long id) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getMemberId,id)
                .eq(GroupMember::getQuit,false);

        return this.list(queryWrapper);
    }

    @Override
    public List<Long> findMemberIdsByGroupId(Long id) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getGroupId,id)
                .eq(GroupMember::getQuit,false);
        List<GroupMember> list = this.list(queryWrapper);
        return list.stream().map(member->member.getMemberId()).collect(Collectors.toList());
    }

    @Override
    public List<GroupMember> findByGroupId(Long id) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getGroupId,id);
        return this.list(queryWrapper);
    }

    @Override
    public GroupMember findByGroupIdAndUserId(Long groupId, Long userId){
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getGroupId,groupId)
                .eq(GroupMember::getMemberId,userId);
        return getOne(queryWrapper);
    }

    @Override
    public void deleteByGroupId(Long id){
        UpdateWrapper<GroupMember> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(GroupMember::getGroupId,id)
                .set(GroupMember::getQuit,true);
        update(updateWrapper);
    }

}
