package com.yjc.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.common.constant.RedisKey;
import com.yjc.platform.constants.JWTConstant;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.mapper.UserMapper;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.util.JWTUtil;
import com.yjc.platform.vo.LoginVO;
import com.yjc.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public LoginVO login(LoginDto userDto) {
        User user = findByUsername(userDto.getUsername());
        if(user == null){

            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "用户不存在");
        }
        if(!encoder.matches(userDto.getPassword(),user.getPassword())){
            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "密码错误");
        }
//        if(!user.getPassword().equals(userDto.getPassword())){
//            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "密码错误");
//        }
        Session session = BeanUtil.copyProperties(user,Session.class);

        String json = JSON.toJSONString(session);
        String authorization = JWTUtil.sign(user.getId(),json, JWTConstant.AUTHORIZATION_EXPIRE,JWTConstant.AUTHORIZATION_SECRET);
        String refreshToken = JWTUtil.sign(user.getId(),json, JWTConstant.REFRESH_TOKEN_EXPIRE,JWTConstant.REFRESH_TOKEN_SECRET);
        LoginVO loginVO = new LoginVO();
        loginVO.setAuthorization(authorization);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setAuthorizationExpiresIn(JWTConstant.AUTHORIZATION_EXPIRE);
        loginVO.setRefreshTokenExpiresIn(JWTConstant.REFRESH_TOKEN_EXPIRE);
        log.info("登陆成功");
        return loginVO;
    }

    @Override
    public void register(LoginDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername,userDto.getUsername());
        User user = getOne(queryWrapper);
        if(user != null){
            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "用户名已注册");
        }

        user = BeanUtil.copyProperties(userDto,User.class);
        user.setNickname(userDto.getUsername());
        user.setPassword(encoder.encode(userDto.getPassword()));
        save(user);
        log.info("注册成功");
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        try {
            JWTUtil.search(refreshToken,JWTConstant.REFRESH_TOKEN_SECRET);
            Long userId = JWTUtil.getId(refreshToken);
            String info = JWTUtil.getInfo(refreshToken);
            String authorization = JWTUtil.sign(userId, info, JWTConstant.AUTHORIZATION_EXPIRE, JWTConstant.AUTHORIZATION_SECRET);
            String newRefreshToken = JWTUtil.sign(userId, info, JWTConstant.REFRESH_TOKEN_EXPIRE, JWTConstant.REFRESH_TOKEN_SECRET);

            LoginVO loginVO = new LoginVO();
            loginVO.setAuthorization(authorization);
            loginVO.setRefreshToken(newRefreshToken);
            loginVO.setAuthorizationExpiresIn(JWTConstant.AUTHORIZATION_EXPIRE);
            loginVO.setRefreshTokenExpiresIn(JWTConstant.REFRESH_TOKEN_EXPIRE);
            return loginVO;
        }
        catch (JWTVerificationException e){

            throw new GlobalException("refreshToken已失效");
        }
    }

    public User findByUsername(String username){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername,username);
        User one = getOne(queryWrapper);
        if(one == null) {
            throw new GlobalException("该用户不存在");
        }
        return one;
    }

    @Transactional
    @Override
    public void update(UserVO userVO) {
        if(!SessionContext.getSession().getId().equals(userVO.getId())){
            throw new GlobalException("不能修改其他用户的信息");
        }
        User user = getById(userVO.getId());
        if (user == null){
            throw new GlobalException("用户不存在");
        }
        if(!user.getNickname().equals(userVO.getNickname()) || !user.getHeadImageThumb().equals(userVO.getHeadImageThumb())){
            List<GroupMember> glist = groupMemberService.findByUserId(userVO.getId());
            for(GroupMember groupMember:glist){
                groupMember.setHeadImage(userVO.getHeadImageThumb());
                groupMember.setMemberNickname(userVO.getNickname());
            }
            groupMemberService.updateBatchById(glist);

        }

        user.setNickname(userVO.getNickname());
        user.setAge(userVO.getAge());
        user.setHeadImage(userVO.getHeadImage());
        user.setSex(userVO.getSex());
        user.setHeadImageThumb(userVO.getHeadImageThumb());
        user.setSignature(userVO.getSignature());
        updateById(user);

    }

    @Override
    public List<Long> checkOnline(String userIds) {
        String[] split = userIds.split(",");
        LinkedList<Long> longs = new LinkedList<>();
        for(String userId:split){
            if(isOnline(Long.parseLong(userId))){
                longs.add(Long.parseLong(userId));
            }
        }
        return longs;
    }

    @Override
    public boolean isOnline(long userId) {
        String key = RedisKey.USER_SEVER_ID + userId;
        Integer serverId = (Integer) redisTemplate.opsForValue().get(key);
        return serverId!=null;
    }
    @Override
    public User findById(Long id){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId,id);
        User one = getOne(queryWrapper);
        if(one == null){
            throw new GlobalException("该用户不存在");
        }
        return one;
    }

}
