package com.yjc.platform.controller;

import com.yjc.platform.pojo.Result;
import com.yjc.platform.service.FileService;
import com.yjc.platform.util.ResultUtil;
import com.yjc.platform.vo.ImageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping("/file")
    @Operation(summary = "上传文件",description = "返回文件url")
    public Result<String> file(@Valid @RequestParam("file") MultipartFile file){
        return ResultUtil.success(fileService.uploadFile(file));
    }
    @PostMapping("/image")
    @Operation(summary = "上传图片",description = "返回原图url和缩略图url")
    public Result<ImageVO> image(@Valid @RequestParam("file") MultipartFile file){
        return ResultUtil.success(fileService.uploadImage(file));
    }
}
