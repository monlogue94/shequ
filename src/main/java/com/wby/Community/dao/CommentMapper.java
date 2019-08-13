package com.wby.Community.dao;

import com.wby.Community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface  CommentMapper {
    //分页查询一个帖子可能有无数个评论
    //为什么传入的是实体呢，可能查询的是问题的评论也有可能是评论的评论
    List<Comment > selectCommentsByEntity(int entityType,int entityId,int offset,int limit );
    // 查询总数
    int selectCountByEntity(int entityType,int entityId);



}
