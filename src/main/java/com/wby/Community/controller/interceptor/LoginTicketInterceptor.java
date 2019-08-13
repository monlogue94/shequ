package com.wby.Community.controller.interceptor;

import com.wby.Community.util.CookieUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //从cookie中获取凭证
String  ticket= CookieUtil.getValue(request ,"ticket");
        return true;
    }
}
