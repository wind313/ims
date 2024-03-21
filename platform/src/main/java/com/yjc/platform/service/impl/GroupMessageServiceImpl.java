package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.mapper.GroupMessageMapper;
import com.yjc.platform.pojo.Group;
import com.yjc.platform.pojo.GroupMessage;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.GroupMessageService;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.vo.GroupMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements GroupMessageService {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMemberService groupMemberService;
    @Override
    public Long send(GroupMessageVO groupMessageVO) {
        Long userId = SessionContext.getSession().getId();
        Group group= groupService.getById(groupMessageVO.getReceiveId());
        if(group == null){
            throw new GlobalException("群聊不存在");
        }
        if (group.getDeleted()){
            throw new GlobalException("群聊已解散");
        }

        List<Long> list = groupMemberService.findMemberIdsByGroupId(groupMessageVO.getReceiveId());
        if(!list.contains(userId)){
            throw new GlobalException("你不在群聊中");
        }
        GroupMessage groupMessage = BeanUtils.copyProperties(groupMessageVO, GroupMessage.class);

        return null;
    }
}
