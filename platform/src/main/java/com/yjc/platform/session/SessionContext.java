package com.yjc.platform.session;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SessionContext {
    public static Session getSession(){
        ServletRequestAttributes attributes = ServletRequestAttributes.class.cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes.getRequest();
        Session session = (Session)request.getAttribute("session");
        return  session;
    }
}
