package com.wby.Community.config;

import com.wby.Community.util.CommunityConstant;
import com.wby.Community.util.CommunityUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements CommunityConstant {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web .ignoring().antMatchers("/resources/**");

    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        http .authorizeRequests().antMatchers("/user/setting",
                "/user/upload",
                "/discuss/add ",
                "/comment/add/**",
                "/letter/**",
                "/notices/**",
                "/like",
                "/follow",
                "/unfollow"

        ).hasAnyAuthority(
    AUTHORITY_USER,
                AUTHORITY_ADMIN,
                AUTHORITY_MODERARTOR
        ).anyRequest().permitAll();
        //权限不够怎么处理
         http .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
            //没有登录的处理
             @Override
             public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
             String xRequestedWith=request .getHeader("x-requested-with");
             if ("XMLHttpRequest".equals(xRequestedWith)){
              response.setContentType("application/plain;charset=utf-8");
                 PrintWriter writer =response .getWriter();
                 writer .write(CommunityUtil.getJSONString(403,"请登录"));

             //else 是同步所以返回html
             }else
                 response.sendRedirect(request.getContextPath()+"/login");
             }
         }).accessDeniedHandler(new AccessDeniedHandler() {
             //权限不足的处理
             @Override
             public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

                 String xRequestedWith = httpServletRequest.getHeader("x-requested-with");
                 if ("XMLHttpRequest".equals(xRequestedWith)) {
                     httpServletResponse.setContentType("application/plain;charset=utf-8");
                     PrintWriter writer = httpServletResponse.getWriter();
                     writer.write(CommunityUtil.getJSONString(403, "无权限"));

                     //else 是同步所以返回html
                 } else
                     httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/denied");
             }


         });
         //security 底层会默认拦截/logout请求，进行退出处理
        //覆盖此代码，执行我们自己写的代码
        http .logout() .logoutUrl("/securitylogout");
    }
}





