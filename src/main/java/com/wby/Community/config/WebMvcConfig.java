package com.wby.Community.config;

import com.wby.Community.controller.interceptor.AlphaInterceptor;
import com.wby.Community.controller.interceptor.LoginTicketInterceptor;
import com.wby.Community.controller.interceptor.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sun.rmi.log.LogInputStream;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AlphaInterceptor alphaInterceptor ;
    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor ;
    @Autowired
    private MessageInterceptor messageInterceptor ;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry .addInterceptor(alphaInterceptor).excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");


        registry .addInterceptor(loginTicketInterceptor ).excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
               ;

        registry .addInterceptor(messageInterceptor  ).excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
        ;


    }
}

