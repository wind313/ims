package com.yjc.platform.controller;

import com.yjc.platform.Exceptions.GlobalException;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.UserVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/self")
    public Result<UserVO> getSelf(){
        Session session = SessionContext.getSession();
        User user = userService.findById(session.getId());
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find/{id}")
    public Result<UserVO> findById(@NotNull(message = "用户id不能为空") @PathVariable("id") Long id){
        User user = userService.findById(id);
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find")
    public Result<UserVO> findByUsername(@NotBlank(message = "用户名不能为空") @RequestParam("username") String username){
        User user = userService.findByUsername(username);

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @PutMapping("/update")
    public Result update(@Valid @RequestBody UserVO userVO){
        userService.update(userVO);
        return ResultUtil.success();
    }



}
