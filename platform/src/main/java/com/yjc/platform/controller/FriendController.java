package com.yjc.platform.controller;

import com.yjc.platform.pojo.Friend;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FriendService;
import com.yjc.platform.session.SessionContext;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.FriendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PutMapping("/add")
    public Result add(@RequestParam("friendId") Long friendId){
        friendService.add(friendId);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    public Result<List<FriendVO>> list(){
        List<Friend> list = friendService.friends(SessionContext.getSession().getId());
        List<FriendVO> l = new ArrayList<>();
        for(Friend friend:list){
            FriendVO friendVO = new FriendVO();
            friendVO.setFriendId(friend.getFriendId());
            friendVO.setFriendNickname(friend.getFriendNickname());
            friendVO.setFriendHeadImage(friend.getFriendHeadImage());
            l.add(friendVO);
        }
        return ResultUtil.success(l);
    }

    @DeleteMapping("/delete/{friendId}")
    public Result delete(@PathVariable("friendId") Long friendId){
        friendService.delete(friendId);
        return ResultUtil.success();
    }
}
