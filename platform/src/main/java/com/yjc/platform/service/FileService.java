package com.yjc.platform.service;

import com.yjc.platform.enums.FileType;
import com.yjc.platform.vo.ImageVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file);
    String getUrl(FileType fileType, String fileName);

    ImageVO uploadImage(MultipartFile file);
}
