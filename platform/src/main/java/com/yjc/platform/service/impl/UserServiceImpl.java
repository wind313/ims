package com.yjc.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjc.platform.Constants.JWTConstant;
import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.mapper.UserMapper;
import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.service.GroupMemberService;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.util.JWTUtil;
import com.yjc.platform.vo.LoginVO;
import com.yjc.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

//    @Autowired
//    private PasswordEncoder encoder;

    @Autowired
    private GroupMemberService groupMemberService;

    @Override
    public LoginVO login(LoginDto userDto) {
        User user = findByUsername(userDto.getUsername());
        if(user == null){

            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "用户不存在");
        }
//        if(!encoder.matches(userDto.getPassword(),user.getPassword())){
//            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "密码错误");
//        }
        if(!user.getPassword().equals(userDto.getPassword())){

            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "密码错误");
        }
        Session session = BeanUtils.copyProperties(user,Session.class);

        String json = JSON.toJSONString(session);
        String accessToken = JWTUtil.sign(user.getId(),json, JWTConstant.ACCESS_TOKEN_EXPIRE,JWTConstant.ACCESS_TOKEN_SECRET);
        String refreshToken = JWTUtil.sign(user.getId(),json, JWTConstant.REFRESH_TOKEN_EXPIRE,JWTConstant.REFRESH_TOKEN_SECRET);
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setAccessTokenExpiresIn(JWTConstant.ACCESS_TOKEN_EXPIRE);
        loginVO.setRefreshTokenExpiresIn(JWTConstant.REFRESH_TOKEN_EXPIRE);
        log.info("登陆成功");
        return loginVO;
    }

    @Override
    public void register(LoginDto userDto) {
        User user = findByUsername(userDto.getUsername());
        if(user != null){
            throw new GlobalException(ResultCode.PROGRAM_ERROR.getCode(), "用户名已注册");
        }

        user = BeanUtils.copyProperties(userDto,User.class);
        user.setNickname(userDto.getUsername());
//        user.setPassword(encoder.encode(userDto.getPassword()));
        save(user);
        log.info("注册成功");
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        try {
            JWTUtil.search(refreshToken,JWTConstant.REFRESH_TOKEN_SECRET);
            Long userId = JWTUtil.getId(refreshToken);
            String info = JWTUtil.getInfo(refreshToken);
            String accessToken = JWTUtil.sign(userId, info, JWTConstant.ACCESS_TOKEN_EXPIRE, JWTConstant.ACCESS_TOKEN_SECRET);
            String newRefreshToken = JWTUtil.sign(userId, info, JWTConstant.REFRESH_TOKEN_EXPIRE, JWTConstant.REFRESH_TOKEN_SECRET);

            LoginVO loginVO = new LoginVO();
            loginVO.setAccessToken(accessToken);
            loginVO.setRefreshToken(newRefreshToken);
            loginVO.setAccessTokenExpiresIn(JWTConstant.ACCESS_TOKEN_EXPIRE);
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

    @Override
    public void update(UserVO userVO) {
        if(!SessionContext.getSession().getId().equals(userVO.getId())){
            throw new GlobalException("不能修改其他用户的信息");
        }
        User user = getById(userVO.getId());
        if (user == null){
            throw new GlobalException("用户不存在");
        }
        if(!user.getNickname().equals(userVO.getNickname()) || user.getHeadImageThumb().equals(userVO.getHeadImageThumb())){
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
