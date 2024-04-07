package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.constants.Constant;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.mapper.GroupMapper;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import com.yjc.platform.vo.InviteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {


    @Autowired
    private UserService userService;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private FriendService friendService;

    @Transactional
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
        groupMember.setHeadImage(user.getHeadImageThumb());
        groupMemberService.save(groupMember);

        GroupVO groupVO = BeanUtil.copyProperties(group,GroupVO.class);
        groupVO.setNicknameInGroup(user.getNickname());
        groupVO.setRemark(name);

        return groupVO;
    }

    @Transactional
    @Override
    public void update(GroupVO groupVO) {
        Long userId = SessionContext.getSession().getId();
        Group group = getById(groupVO.getId());
        if(userId == group.getOwnerId()){
            if(groupVO.getOwnerId() != userId && groupMemberService.findByGroupIdAndUserId(groupVO.getId(),groupVO.getOwnerId())==null){
                throw new GlobalException("对方不在群内，不能成为群主");
            }
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
    @Transactional
    public void delete(Long id) {
        Long userId = SessionContext.getSession().getId();
        Group group = getById(id);
        Long ownerId = group.getOwnerId();
        if(ownerId == null || group.getDeleted()){
            throw new GlobalException("群聊不存在");
        }
        if(ownerId != userId){
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

        List<GroupMember> members = groupMemberService.findByGroupId(id).stream().filter(member->!member.getQuit()).collect(Collectors.toList());

        List<GroupMemberVO> collect = members.stream()
                .map(member -> {
                    GroupMemberVO vo = BeanUtil.copyProperties(member, GroupMemberVO.class);
                    return vo;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public void quit(Long id) {
        Long userId = SessionContext.getSession().getId();
        Group group = getById(id);
        if(group.getOwnerId() == userId){
            throw new GlobalException("群主不能退出群聊");
        }
        GroupMember member = groupMemberService.findByGroupIdAndUserId(id, userId);
        if(member == null) {
            throw new GlobalException("你不是群成员");
        }
        member.setQuit(true);
        groupMemberService.updateById(member);

    }

    @Override
    public void kick(Long groupId, Long userId) {
        Session session = SessionContext.getSession();
        Group group = getById(groupId);
        if(session.getId()!=group.getOwnerId()){
            throw new GlobalException("你不是群主");
        }
        if(session.getId() == userId){
            throw new GlobalException("不能将群主踢出群聊");
        }
        GroupMember member = groupMemberService.findByGroupIdAndUserId(groupId, userId);
        if(member == null || member.getQuit()){
            throw new GlobalException("用户不在群聊内");
        }
        member.setQuit(true);
        groupMemberService.updateById(member);
    }

    @Override
    public List<GroupVO> getList() {
        Long userId = SessionContext.getSession().getId();
        List<GroupMember> groupMembers = groupMemberService.findByUserId(userId);
        if(groupMembers == null || groupMembers.size() == 0){
            return Collections.EMPTY_LIST;
        }
        List<Long> ids = groupMembers.stream().map(groupMember -> groupMember.getGroupId()).collect(Collectors.toList());
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(Group::getId,ids);
        List<Group> list = list(queryWrapper);
        List<GroupVO> collect = list.stream().map(group -> {
            GroupVO groupVO = BeanUtil.copyProperties(group, GroupVO.class);
            GroupMember member = groupMembers.stream()
                    .filter(groupMember -> groupMember.getGroupId().equals(group.getId())).findFirst().get();
            groupVO.setRemark(member.getRemark().length() == 0?group.getName():member.getRemark());
            groupVO.setNicknameInGroup(member.getNicknameInGroup());
            return groupVO;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void invite(InviteVO inviteVO) {
        Long userId = SessionContext.getSession().getId();

        Group group = getById(inviteVO.getGroupId());
        if(group == null){
            throw new GlobalException("群聊不存在");
        }
        if(groupMemberService.findByGroupIdAndUserId(inviteVO.getGroupId(),userId) == null){
            throw new GlobalException("你不是群成员，无法邀请");
        }
        if(inviteVO.getIds().contains(userId)){
            throw new GlobalException("不能邀请自己");
        }

        List<GroupMember> members = groupMemberService.findByGroupId(inviteVO.getGroupId());
        long size = members.stream().filter(member->!member.getQuit()).count();
        if(size + (long) inviteVO.getIds().size() > Constant.MAX_GROUP_MEMBER){
            throw new GlobalException("群聊人数不能大于"+Constant.MAX_GROUP_MEMBER+"人，邀请失败");
        }

        List<Friend> friends = friendService.findByUserId(userId);
        try{
            inviteVO.getIds().stream()
                    .map(id -> friends.stream().filter(friend -> friend.getFriendId().equals(id)).findFirst().get())
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new GlobalException("部分用户不是你的好友，邀请失败");
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(User::getId,inviteVO.getIds());
        List<User> list = userService.list(queryWrapper);
        List<GroupMember> collect = inviteVO.getIds().stream().map(c -> {
            Optional<GroupMember> first = members.stream().filter(member -> member.getMemberId().equals(c)).findFirst();
            GroupMember member = first.isPresent()?first.get():new GroupMember();
            User user1 = list.stream().filter(user -> user.getId().equals(c)).findFirst().get();
            member.setMemberNickname(user1.getNickname());
            member.setGroupId(inviteVO.getGroupId());
            member.setMemberId(user1.getId());
            member.setHeadImage(user1.getHeadImageThumb());
            member.setQuit(false);
            return member;
        }).collect(Collectors.toList());
        groupMemberService.saveOrUpdateBatch(collect);
    }
}
