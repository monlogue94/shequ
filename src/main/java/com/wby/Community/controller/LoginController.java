package com.wby.Community.controller;

import com.sun.deploy.net.HttpResponse;
import com.wby.Community.entity.User;
import com.wby.Community.service.UserService;
import com.wby.Community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    UserService userService;
    @Value("${server.servlet.context-path}")
    private  String contextPath;

    // 这个方法用于点击注册按钮就显示注册页面给浏览器返回html
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }


    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }


    //这个方法用于提交数据

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    //往model里面传数据然后给模版
    public String register(Model model, User user) throws MessagingException, IllegalAccessException {
        Map<String, Object> map = userService.register(user);
        // 为空说明注册成功
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，注意查看邮箱来激活");
            model.addAttribute("target", "/index");
            return "/site/operate-result";

        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "哥你激活成功了");
            //跳到登录页面
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "激活两次干嘛呀？？？？？？？？？？");
            model.addAttribute("target", "/index");
        } else {


        }
        return "/site/operate-result";
    }
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKapthca(HTTPServletResponse )

}
//页面把这些东西传给我，我给service层处理
@RequestMapping(path = "login", method = RequestMethod.POST)
public  String login(String  username, String password, String  code, boolean rememberme, Model model , HttpSession session , HttpResponse response ) {

    String kaptcha = (String) session.getAttribute("kaptcha");

    if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
        model.addAttribute("codeMsg", "验证码不对哦");
        return "/site/login";
    }

    //检查账号密码交给service层处理
    //这种格式看是否勾选记住我
    int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
    Map<String, Object> map = userService.login(username, password, expiredSeconds);
    if (map.containsKey("ticket")) {
        Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
         cookie.setPath("contextPath");
        cookie.setMaxAge(expiredSeconds);
        response.addCookie(cookie);
        return "redirect:/index";

    } else {
        model.addAttribute("usernameMsg", map.get("usernameMsg"));
        model.addAttribute("pssswordMsg", map.get("passwordMsg"));
        return "/site/login";
    }

}
@RequestMapping(path = "logout", method = RequestMethod.GET )
public  String logout(@CookieValue("ticket") String  ticket){
userService .logout(ticket );
return "redirect:/login";


}
