package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.mapper.GroupMapper;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {


    @Autowired
    private UserService userService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Override
    public GroupVO create(String name) {
        Long userId = SessionContext.getSession().getId();
        User user = userService.getById(userId);

        Group group = new Group();
        group.setName(name);
        group.setImage(user.getHeadImage());
        group.setImageThumb(user.getHeadImageThumb());
        group.setOwnerId(userId);
        save(group);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(group.getId());
        groupMember.setMemberId(userId);
        groupMember.setMemberNickname(user.getNickname());
        groupMember.setRemark(name);
        groupMember.setHeadImage(user.getHeadImageThumb());
        groupMemberService.save(groupMember);

        GroupVO groupVO = BeanUtil.copyProperties(group,GroupVO.class);
        groupVO.setNicknameInGroup(user.getNickname());

        return groupVO;
    }

    @Override
    public void update(GroupVO groupVO) {
        Long userId = SessionContext.getSession().getId();
        Group group = getById(groupVO.getId());
        if(userId == group.getOwnerId()){
            Group group1 = BeanUtil.copyProperties(groupVO, Group.class);
            updateById(group1);
        }
        GroupMember member = groupMemberService.findByGroupIdAndUserId(group.getId(), userId);
        if(member == null) {
            throw new GlobalException("您不是群成员");
        }
        member.setNicknameInGroup(groupVO.getNicknameInGroup());
        member.setRemark(groupVO.getRemark());
        groupMemberService.updateById(member);
    }

    @Override
    public void delete(Long id) {
        Long userId = SessionContext.getSession().getId();
        Group group = getById(id);
        if(group.getOwnerId() != userId){
            throw new GlobalException("你不是群主，无法解散群聊");
        }
        group.setDeleted(true);
        updateById(group);
        groupMemberService.deleteByGroupId(id);

    }

    @Override
    public GroupVO findById(Long id) {
        Group group = getById(id);
        if(group.getDeleted()){
            throw new GlobalException("此群已解散");
        }
        Long userId = SessionContext.getSession().getId();
        GroupMember member = groupMemberService.findByGroupIdAndUserId(id, userId);
        if(member == null || member.getQuit()){
            throw new GlobalException("你不是群成员");
        }

        GroupVO groupVO = BeanUtil.copyProperties(group, GroupVO.class);
        groupVO.setNicknameInGroup(member.getNicknameInGroup());
        groupVO.setRemark(member.getRemark());
        return groupVO;
    }

    @Override
    public List<GroupMemberVO> findMemberByGroupId(Long id) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getGroupId,id)
                .eq(GroupMember::getQuit,false);
        List<GroupMemberVO> collect = groupMemberService.list(queryWrapper).stream()
                .map(member -> {
                    GroupMemberVO vo = BeanUtil.copyProperties(member, GroupMemberVO.class);
                    return vo;
                })
                .collect(Collectors.toList());
        return collect;
    }
}
