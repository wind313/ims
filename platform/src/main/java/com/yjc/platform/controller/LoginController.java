package com.yjc.platform.controller;

import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.UserService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.LoginVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDto userDto){
        LoginVO userVO = userService.login(userDto);
        return ResultUtil.success(userVO);
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody LoginDto userDto){
        userService.register(userDto);
        return ResultUtil.success();
    }

    @PutMapping("/refreshToken")
    public Result<LoginVO> refreshToken(@NotBlank(message = "refreshToken不能为空") @RequestHeader("refreshToken") String refreshToken){
        LoginVO loginVo =userService.refreshToken(refreshToken);
        return ResultUtil.success(loginVo);
    }
}
