package com.wby.Community.controller;

import com.wby.Community.Event.EventProducer;
import com.wby.Community.entity.Comment;
import com.wby.Community.entity.DiscussPost;
import com.wby.Community.entity.Event;
import com.wby.Community.entity.User;
import com.wby.Community.service.CommentService;
import com.wby.Community.service.DiscussPostService;
import com.wby.Community.util.CommunityConstant;
import com.wby.Community.util.CommunityUtil;
import com.wby.Community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

import static com.wby.Community.util.CommunityConstant.ENTITY_TYPE_POST;
import static com.wby.Community.util.CommunityConstant.TOPIC_PUBLISH;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    private DiscussPostService discussPostService;

    @RequestMapping(path = "/add/{discussPostId}", method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        //增加了发帖之后，触发评论事件
        User user = hostHolder.getUser();

        Event event = new Event().setTopic(TOPIC_PUBLISH).setUserId(user.getId()).setEntityType(comment.getEntityType())
                .setEntityId(comment.getId()).setData("postId", discussPostId);
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setUserId(target.getUserId());
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.fireEvent(event);

        return "redirect:/discuss/detail/" + discussPostId;
    }


}