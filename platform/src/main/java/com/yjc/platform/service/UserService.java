package com.yjc.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.pojo.User;
import com.yjc.platform.vo.LoginVO;
import com.yjc.platform.vo.UserVO;

public interface UserService extends IService<User> {


    LoginVO login(LoginDto userDto);

    void register(LoginDto userDto);

    LoginVO refreshToken(String refreshToken);
    User findById(Long id);
    User findByUsername(String username);

    void update(UserVO userVO);
}
