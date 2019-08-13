package com.wby.Community.service;

import com.wby.Community.dao.DiscussPostMapper;
import com.wby.Community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    DiscussPostMapper discussPostMapper ;

   //查询出帖子会显示里面的属性userid ，但我们要显示的是姓名，提供一个方法根据ID查到姓名
    public List<DiscussPost>  findDsicussPsots(int userId,int offset,int limit){
        return discussPostMapper .selectDiscussPosts(userId ,offset ,limit );
    }
    public  int  findDiscussPostRows(int userId){
        return discussPostMapper .selectDiscussPostRows(userId ) ;
    }




   //发布帖子
    public int   addDiscussPost(DiscussPost  discussPost ) throws IllegalAccessException {
        if (discussPost==null){
            throw  new IllegalAccessException(" 参数不能为空");
        }
        return discussPostMapper.insertDiscussPost(discussPost );

    }
    //查询帖子详情
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper .selectDiscussPostById(id);
    }

}
