package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.pojo.User;
import com.yjc.platform.vo.LoginVO;
import com.yjc.platform.vo.UserVO;

import java.util.List;

public interface UserService extends IService<User> {


    LoginVO login(LoginDto userDto);

    void register(LoginDto userDto);

    LoginVO refreshToken(String refreshToken);

    boolean isOnline(long userId);

    UserVO findById(Long id);
    User findByUsername(String username);

    UserVO findUserVOByUsername(String username);

    void update(UserVO userVO);

    List<Long> checkOnline(String userIds);

    boolean isCompleteInfo();
}
