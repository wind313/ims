package com.yjc.platform.controller;

import com.yjc.platform.pojo.GroupMember;
import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.GroupService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.GroupMemberVO;
import com.yjc.platform.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public Result<GroupVO> create(@RequestParam("name") String name){
        return ResultUtil.success(groupService.create(name));
    }

    @PutMapping("/update")
    public Result update(@RequestBody GroupVO groupVO){
        groupService.update(groupVO);
        return ResultUtil.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id){
        groupService.delete(id);
        return ResultUtil.success();
    }

    @GetMapping("/find/{id}")
    public Result<GroupVO> findById(@PathVariable("id") Long id){
        return ResultUtil.success(groupService.findById(id));
    }

    @GetMapping("/members/{id}")
    public Result<List<GroupMemberVO>> findMemberByGroupId(@PathVariable("id") Long id){
        return ResultUtil.success(groupService.findMemberByGroupId(id));
    }
}
