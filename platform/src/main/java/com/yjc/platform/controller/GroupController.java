package com.yjc.platform.controller;

import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import com.yjc.platform.vo.InviteVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Result<GroupVO> create(@NotBlank(message = "群名不能为空") @RequestParam("name") String name){
        return ResultUtil.success(groupService.create(name));
    }

    @PutMapping("/update")
    public Result update(@Valid @RequestBody GroupVO groupVO){
        groupService.update(groupVO);
        return ResultUtil.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        groupService.delete(id);
        return ResultUtil.success();
    }

    @GetMapping("/find/{id}")
    public Result<GroupVO> findById(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        return ResultUtil.success(groupService.findById(id));
    }

    @GetMapping("/members/{id}")
    public Result<List<GroupMemberVO>> findMemberByGroupId(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        return ResultUtil.success(groupService.findMemberByGroupId(id));
    }

    @DeleteMapping("/quit/{id}")
    public Result quit(@NotNull(message = "群聊id不能为空") @PathVariable("id") Long id){
        groupService.quit(id);
        return ResultUtil.success();
    }

    @DeleteMapping("/kick/{groupId}")
    public Result kick(@NotNull(message = "群id不能为空") @PathVariable("groupId") Long groupId,@NotNull(message = "用户id不能为空") @RequestParam("userId") Long userId){
        groupService.kick(groupId,userId);
        return ResultUtil.success();
    }

    @GetMapping("/list")
    public Result<List<GroupVO>> list(){
        return ResultUtil.success(groupService.getList());
    }

    @PostMapping("/invite")
    public Result invite(@Valid @RequestBody InviteVO inviteVO){
        groupService.invite(inviteVO);
        return ResultUtil.success();
    }



}
