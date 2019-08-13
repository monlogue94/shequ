package com.wby.Community.controller;

import com.wby.Community.entity.User;
import com.wby.Community.service.UserService;
import com.wby.Community.util.CommunityUtil;
import com.wby.Community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;


    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;



    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model ) throws IOException {
        if(headerImage ==null){
            model .addAttribute("error","选一张图片啦");
            return "/site/setting";
        }
       String  fileName= headerImage.getOriginalFilename();
        String suffix=fileName .substring(fileName .lastIndexOf("."));
     if (StringUtils.isBlank(suffix)){

         model .addAttribute("error"," 图片哪里找的，不适合未成年哦");
         return "/site/setting";
     }
     //生成随机文件名加后缀
        fileName= CommunityUtil.generateUUID();
     //把文件存到指定位置
     File  dest=new File(uploadPath+"/"+fileName) ;
      headerImage .transferTo(dest );
      //到这里了说明上传成功
        User user =hostHolder.getUser();
        String headUrl =domain +contextPath+"/user/header/"+fileName;
        userService.updateHeader(user.getId(),headUrl );
        return "redirect/index";
    }
    @RequestMapping(path = "/header/{fileName}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName")String fileName, HttpServletResponse response ) throws IOException {
       fileName= uploadPath +"/"+fileName;
       // 图片文件后缀
        String suffix=fileName .substring(fileName .lastIndexOf("."));
        //响应图片
         response.setContentType("image/"+suffix);
          OutputStream os=response.getOutputStream();
        FileInputStream fis=new FileInputStream(fileName );
        byte [] buffer=new byte[1024] ;
        int b =0;
        while((b=fis.read(buffer) )!=-1){
            os.write(buffer ,0,b );
        }
    }
}
