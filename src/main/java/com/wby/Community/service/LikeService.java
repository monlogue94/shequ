package com.wby.Community.service;

import com.wby.Community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;


public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate ;
// 点赞
    public void like(int userId,int entityType,int entityId){
        String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType ,entityId );
        //第一次为点赞第二次为取消要先判断userid在不在集合里面，不咋就没有点赞
  boolean isMember= redisTemplate .opsForSet().isMember(entityLikeKey,userId );
  if(isMember ){
      redisTemplate.opsForSet().remove(entityLikeKey,userId) ;

  }else{
      redisTemplate.opsForSet().add(entityId ,userId );
  }

    }
    //redis如何保证事务，因为有两次更新操作，要保证正确,有两个key


    RedisTemplate .excute(RedisOperations operations )thro



    // 查询点赞数量
    public  long findEntityLikeCount(int entityType,int entityId ){
        String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType ,entityId );
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询某人对某个实体有没有点赞
     public int findEntityLikeStatus(int userId,int entityType,int entityId){
         String entityLikeKey= RedisKeyUtil.getEntityLikeKey(entityType ,entityId );
         return redisTemplate .opsForSet().isMember(entityLikeKey,userId )? 1:0;

     }
     //查询某人的获得的赞
    public int findUsrLikeCount(int userId){
        String entityLikeKey= RedisKeyUtil.getUserLikeKey(userId );
        redisTemplate.opsForValue().get(userLikeKey);
    }



}
