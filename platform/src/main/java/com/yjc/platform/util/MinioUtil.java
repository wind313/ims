package com.yjc.platform.util;


import com.yjc.platform.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

@Slf4j
@Component
public class MinioUtil {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioConfig prop;

    public String upload(String path, MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null){
            throw new RuntimeException();
        }
        String objectName = DateTimeUtil.getFormatDate(new Date(),DateTimeUtil.PART_DATA_TIME)+"/"
                +System.currentTimeMillis()+originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            PutObjectArgs build = PutObjectArgs.builder()
                    .bucket(prop.getBucketName()).object(path+"/"+objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType()).build();
            minioClient.putObject(build);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return objectName;
    }

    public String upload(String path,String name,byte[] fileBytes,String contentType){
        String fileNmae = System.currentTimeMillis() + name.substring(name.lastIndexOf("."));
        String objectName = DateTimeUtil.getFormatDate(new Date(), DateTimeUtil.PART_DATA_TIME) + "/" + fileNmae;
        try{
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(path + "/" + objectName)
                    .stream(inputStream, fileBytes.length, -1)
                    .contentType(contentType).build();
            minioClient.putObject(args);
        }
        catch (Exception e){
            log.error("上传失败",e);
            return null;
        }
        return objectName;
    }



}
