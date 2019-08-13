package com.wby.Community.controller;

import com.wby.Community.entity.User;
import com.wby.Community.service.LikeService;
import com.wby.Community.util.CommunityUtil;
import com.wby.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;
    //当前用户谁点赞
    @Autowired
    private HostHolder hostHolder;

    //处理异步请求

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    //异步请求需要这个注解
    @ResponseBody
    public String like(int entityType, int entityId) {
        User user = hostHolder.getUser();
        likeService.like(user.getId(), entityType, entityId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

//状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

//把状态和数量封装给map，再给页面
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        return CommunityUtil.getJSONString(0, null, map);


    }
}
