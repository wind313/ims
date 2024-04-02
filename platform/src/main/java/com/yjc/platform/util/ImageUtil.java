package com.yjc.platform.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
public class ImageUtil {

    private static final Integer ZERO = 0 ;
    private static final Integer ONE_ZERO_TWO_FOUR = 1024 ;
    private static final Integer NINE_ZERO_ZERO = 900 ;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275 ;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047 ;
    private static final Double ZERO_EIGHT_FIVE = 0.85 ;
    private static final Double ZERO_SIX = 0.6 ;
    private static final Double ZERO_FOUR_FOUR = 0.44 ;
    private static final Double ZERO_FOUR = 0.4 ;

    public static boolean isImage(String fileName){
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String[] imageExtension = new String[]{"jpg","jpeg","png","bmp","webp","gif"};
        for(String ie:imageExtension){
            if(ie.equals(extension)) return true;
        }
        return false;
    }

    public static byte[] compress(byte[] imageBytes,long desFileSize){

        if(imageBytes == null || imageBytes.length<=ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR){
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try{
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR){
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
            log.info("图片大小={}kb，压缩后大小={}kb",srcSize/ONE_ZERO_TWO_FOUR,imageBytes.length/ONE_ZERO_TWO_FOUR);
        }
        catch (Exception e){
            log.error("图片压缩失败",e);
        }
        return imageBytes;

    }

    private static double getAccuracy(long size){
        double accuracy;
        if(size < NINE_ZERO_ZERO){
            accuracy = ZERO_EIGHT_FIVE;
        }
        else if(size < TWO_ZERO_FOUR_SEVEN){
            accuracy = ZERO_SIX;
        }
        else if(size < THREE_TWO_SEVEN_FIVE){
            accuracy = ZERO_FOUR_FOUR;
        }
        else{
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }

}
