package com.wby.Community.dao;

import com.wby.Community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    //本来首页的帖子是全部显示的，这里要id为了以后显示用户的帖子
    //ID为0就是默认为全部帖子，为2，34，这种就是用户的帖子所以是动态sql
// offset行号 ，limit 每页显示的条数

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询出一个有多少帖子 也是动态sql     "这是别名"
    // 动态sql中需要用到这个参数，这个方法只有一个参数，必须要别名。上面的方法三个参数所以不需要别名
    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

//到这里完成了首页的查询功能呢


    //增加帖子的方法
    int insertDiscussPost(DiscussPost discussPost);

    //  根据ID查询帖子详情
    DiscussPost selectDiscussPostById(int id);


    int updateCommentCount(int id, int commentCount);
}