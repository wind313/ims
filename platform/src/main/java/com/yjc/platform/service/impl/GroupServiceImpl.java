package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.mapper.GroupMapper;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        GroupVO groupVO = BeanUtils.copyProperties(group,GroupVO.class);
        groupVO.setRemark(name);
        groupVO.setNicknameInGroup(user.getNickname());

        return groupVO;
    }
}
