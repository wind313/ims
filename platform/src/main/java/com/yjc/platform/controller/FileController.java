package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FileService;
import com.yjc.platform.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    public Result<String> file(@RequestParam("file") MultipartFile file){
        return ResultUtil.success(fileService.uploadFile(file));
    }
}