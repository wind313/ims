package com.yjc.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.constants.RedisKey;
import com.yjc.platform.mapper.GroupMemberMapper;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = RedisKey.IM_MEMBER)
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public boolean save(GroupMember groupMember){
        redisTemplate.delete(RedisKey.IM_MEMBER+"::"+groupMember.getGroupId());
        return super.save(groupMember);
    }


    @Override
    public boolean saveOrUpdateBatch(Long id,List<GroupMember> entityList) {
        redisTemplate.delete(RedisKey.IM_MEMBER+"::"+id);
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    public void removeMember(Long groupId, Long userId) {
        UpdateWrapper<GroupMember> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(GroupMember::getGroupId,groupId)
                        .eq(GroupMember::getMemberId,userId)
                                .set(GroupMember::getQuit,true);
        update(updateWrapper);
        redisTemplate.delete(RedisKey.IM_MEMBER+"::"+groupId);
    }

    @Override
    public List<GroupMember> findByUserId(Long id) {
        QueryWrapper<GroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GroupMember::getMemberId,id)
                .eq(GroupMember::getQuit,false);

        return this.list(queryWrapper);
    }

    @Cacheable(key = "#p0")
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
    @CacheEvict(key = "#p0")
    public void deleteByGroupId(Long id){
        UpdateWrapper<GroupMember> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(GroupMember::getGroupId,id)
                .set(GroupMember::getQuit,true);
        update(updateWrapper);
        redisTemplate.delete(RedisKey.IM_MEMBER+"::"+id);
    }

}
