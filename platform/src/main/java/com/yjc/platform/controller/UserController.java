package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.pojo.User;
import com.yjc.platform.service.UserService;
import com.yjc.platform.session.Session;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.BeanUtils;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.UserVO;
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
        UserVO userVO = BeanUtils.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find/{id}")
    public Result<UserVO> findById(@PathVariable("id") Long id){
        User user = userService.findById(id);
        UserVO userVO = BeanUtils.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @GetMapping("/find")
    public Result<UserVO> findByUsername(@RequestParam("username") String username){
        User user = userService.findByUsername(username);
        UserVO userVO = BeanUtils.copyProperties(user, UserVO.class);
        return ResultUtil.success(userVO);
    }
    @PutMapping("/update")
    public Result update(@RequestBody UserVO userVO){
        userService.update(userVO);
        return ResultUtil.success();
    }



}
