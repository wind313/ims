package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtil;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/online")
    @Operation(summary = "查询在线状态",description = "查询在线用户id集合")
    public Result checkOnline(@NotBlank(message = "userIds不为空") @RequestParam("userIds") String userIds){
        List<Long> onlineIds = userService.checkOnline(userIds);
        return ResultUtil.success(onlineIds);
    }

    @GetMapping("/self")
    @Operation(summary = "查询自己信息",description = "获取自己的信息")
    public Result<UserVO> getSelf(){
        Session session = SessionContext.getSession();
        User user = userService.findById(session.getId());
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find/{id}")
    @Operation(summary = "查找用户",description = "根据id查找用户")
    public Result<UserVO> findById(@NotNull(message = "用户id不能为空") @PathVariable("id") Long id){
        User user = userService.findById(id);
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find")
    @Operation(summary = "查找用户",description = "根据用户名查找用户")
    public Result<UserVO> findByUsername(@NotBlank(message = "用户名不能为空") @RequestParam("username") String username){
        User user = userService.findByUsername(username);

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @PutMapping("/update")
    @Operation(summary = "修改用户信息",description = "修改自己的信息")
    public Result update(@Valid @RequestBody UserVO userVO){
        userService.update(userVO);
        return ResultUtil.success();
    }



}
