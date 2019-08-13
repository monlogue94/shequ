package com.wby.Community.controller;

import com.wby.Community.entity.DiscussPost;
import com.wby.Community.entity.Page;
import com.wby.Community.entity.User;
import com.wby.Community.service.CommentService;
import com.wby.Community.service.DiscussPostService;
import com.wby.Community.service.LikeService;
import com.wby.Community.service.UserService;
import com.wby.Community.util.CommunityConstant;
import com.wby.Community.util.CommunityUtil;
import com.wby.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    CommentService commentService ;
    @Autowired
    LikeService likeService ;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) throws IllegalAccessException {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, " 哥登录一下");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);
        return CommunityUtil.getJSONString(0, "发布成功");


    }

    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page  page ) {
        //帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        //作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
        //点赞数量
        long  likeCount =likeService.findEntityLikeCount(ENTITY_TYPE_POST,discussPostId );
        model .addAttribute("likeCount",likeCount ) ;
        //点赞状态
        int  likeStatus=hostHolder .getUser() ==null ? 0:likeService.findEntityLikeStatus(hostHolder .getUser().getId() ,ENTITY_TYPE_POST,discussPostId );
        model .addAttribute("likeStatus",likeStatus);
     //查评论的分页信息,
        page.setLimit(5);
        page.setPath( );
        return "/site/discuss-detail";

    }
}
