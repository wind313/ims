package com.yjc.platform.controller;

import com.yjc.platform.dto.LoginDto;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.UserService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "登陆注册")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录",description = "返回accessToken和refreshToken")
    public Result<LoginVO> login(@Valid @RequestBody LoginDto userDto){
        LoginVO userVO = userService.login(userDto);
        return ResultUtil.success(userVO);
    }

    @PostMapping("/register")
    @Operation(summary = "注册",description = "用户注册")
    public Result register(@Valid @RequestBody LoginDto userDto){
        userService.register(userDto);
        return ResultUtil.success();
    }

    @PutMapping("/refreshToken")
    @Operation(summary = "刷新token",description = "使用refreshToken换取新的accessToken")
    public Result<LoginVO> refreshToken(@NotBlank(message = "refreshToken不能为空") @RequestHeader("refreshToken") String refreshToken){
        LoginVO loginVo =userService.refreshToken(refreshToken);
        return ResultUtil.success(loginVo);
    }
}
