package com.yjc.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.yjc.platform.constants.JWTConstant;
import com.yjc.platform.exceptions.GlobalException;
import com.yjc.platform.enums.ResultCode;
import com.yjc.platform.session.Session;
import com.yjc.platform.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if(token == null){
            throw new GlobalException(ResultCode.NOT_LOGIN);
        }
        try{
            JWTUtil.search(token, JWTConstant.AUTHORIZATION_SECRET);
        }
        catch (JWTVerificationException e){
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        String info = JWTUtil.getInfo(token);
        Session session = JSON.parseObject(info,Session.class);
        request.setAttribute("session",session);
        return true;
    }

}
