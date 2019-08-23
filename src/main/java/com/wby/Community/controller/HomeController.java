package com.wby.Community.controller;

import com.wby.Community.entity.DiscussPost;
import com.wby.Community.entity.Page;
import com.wby.Community.entity.User;
import com.wby.Community.service.DiscussPostService;
import com.wby.Community.service.LikeService;
import com.wby.Community.service.UserService;
import com.wby.Community.util.CommunityConstant;
import com.wby.Community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostServic;
    @Autowired
    private UserService userService;
    @Autowired
    LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)

    public String getIndexPage(Model model, Page page) {
        page.setRows(discussPostServic.findDiscussPostRows(0));
        page.setPath("/index");


        List<DiscussPost> list = discussPostServic.findDsicussPsots(0, page.getOffset(), page.getLimit());
        //上面返回的帖子中是userid不是名字
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);

                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                //首页选出了10个帖子，每个帖子旁边显示获得多少赞
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        //"discussPosts"相当于别名

        return "/index";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "/erroer/404";
    }
}
