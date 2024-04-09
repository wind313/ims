package com.yjc.platform.service.impl;

import com.yjc.platform.constants.MinioConstant;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.config.MinioConfig;
import com.yjc.platform.enums.FileType;
import com.yjc.platform.service.FileService;
import com.yjc.platform.util.ImageUtil;
import com.yjc.platform.util.MinioUtil;
import com.yjc.platform.vo.ImageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FileServiceImpl implements FileService {



    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    MinioUtil minioUtil;
    @Override
    public String uploadFile(MultipartFile file) {
        if(file.getSize() > MinioConstant.MAX_FILE_SIZE){
            throw new GlobalException("文件不能大于10MB");
        }
        String upload = minioUtil.upload(MinioConstant.FILE_PATH, file);
        if(StringUtils.isEmpty(upload)){
            throw new GlobalException("上传失败");
        }

        return getUrl(FileType.FILE,upload);
    }
    @Override
    public ImageVO uploadImage(MultipartFile file) {
        try{
            if(file.getSize() > MinioConstant.MAX_IMAGE_SIZE){
                throw new GlobalException("图片不能大于5MB");
            }
            if(!ImageUtil.isImage(file.getOriginalFilename())){
                throw new GlobalException("图片格式不合法");
            }
            String upload = minioUtil.upload(MinioConstant.IMAGE_PATH, file);
            if(StringUtils.isEmpty(upload)){
                throw new GlobalException("上传失败");
            }

            String url = getUrl(FileType.IMAGE, upload);
            ImageVO imageVO = new ImageVO();
            imageVO.setUrl(url);

            byte[] compress = ImageUtil.compress(file.getBytes(), 100);
            upload = minioUtil.upload(MinioConstant.IMAGE_PATH, file.getOriginalFilename(), compress, file.getContentType());
            if(StringUtils.isEmpty(upload)){
                throw new GlobalException("上传失败");
            }
            imageVO.setThumbUrl(getUrl(FileType.IMAGE,upload));
            return imageVO;
        }catch (Exception e){
            log.error("上传失败，{}",e.getMessage(),e);
            throw new GlobalException("图片上传失败");
        }


    }
    @Override
    public String getUrl(FileType fileType, String fileName){
        String url = minioConfig.getEndpoint()+"/"+minioConfig.getBucketName();
        switch (fileType){
            case IMAGE:
                url += "/image/";
                break;
            case FILE:
                url += "/file/";
                break;
            case VIDEO:
                url += "/video/";
        }

        url += fileName;
        return url;
    }



}
