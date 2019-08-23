 package com.wby.Community.controller;

import com.wby.Community.Event.EventProducer;
import com.wby.Community.entity.Event;
import com.wby.Community.entity.User;
import com.wby.Community.service.LikeService;
import com.wby.Community.util.CommunityConstant;
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
public class LikeController implements CommunityConstant {
    @Autowired
    private LikeService likeService;
    //当前用户谁点赞
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer ;

    //处理异步请求

    @RequestMapping(path = "/like", method = RequestMethod.POST)
    //异步请求需要这个注解
    @ResponseBody
    public String like(int entityType, int entityId,int entityUserId,int postId) {
        User user = hostHolder.getUser();
       likeService.like(user.getId() ,entityType,entityId,entityUserId );
        // likeService.like(user.getId(), entityType, entityId);
        //数量
        long likeCount = likeService.findEntityLikeCount(entityType, entityId);

//状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

//把状态和数量用map组合到一起，再给页面。这里是异步刷新所以没有模版，直接把map变成json数据格式返回
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);
        //触发点赞通知
       if (likeStatus==1){
        Event event =new Event() .setTopic(TOPIC_LIKE)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(entityType)
                .setEntityId(entityId)
                .setEntityUserId(entityUserId )
                .setData("postId", postId);
        eventProducer.fireEvent(event);
    }


        return CommunityUtil.getJSONString(0, null, map);


    }
}
