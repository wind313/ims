package com.yjc.platform.util;

import org.springframework.util.ReflectionUtils;

public class BeanUtil {
    public static <T> T copyProperties(Object obj,Class<T> clazz) {
        try{
            Object t = clazz.newInstance();
            if(obj == null) return null;
            copyProperties(obj,t);
            return (T)t;
        } catch (Exception e){
            ReflectionUtils.handleReflectionException(e);
            return null;
        }

    }
    public static void copyProperties(Object obj,Object t) {
        try{
            org.springframework.beans.BeanUtils.copyProperties(obj,t);
        } catch (Exception e){
            ReflectionUtils.handleReflectionException(e);
        }

    }

}
