package com.yjc.server.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware
{
    @Autowired
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }
    public static ApplicationContext getApplicationContext(){
        if(SpringContextHolder.applicationContext == null){
            throw new RuntimeException("applicationContext为空");
        }
        return applicationContext;
    }
}
