package com.wby.Community.service;

import com.wby.Community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;


public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    // 点赞
    public void like(int userId, int entityType, int entityId, int entityUserId) {
        //String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        //第一次为点赞第二次为取消要先判断userid在不在集合里面，不然就没有点赞
        //boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        //if (isMember) {
        // redisTemplate.opsForSet().remove(entityLikeKey, userId);

        // } else {
        //redisTemplate.opsForSet().add(entityLikeKey, userId);
        // }


        //我收到的赞，以用户为key，还在like方法里面写
        //redis如何保证事务，因为有两次更新操作增加userid，要保证正确,有两个key
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
                boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
                //开启事务
                operations.multi();
                if (isMember) {
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                } else {
                    operations.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }
                //执行事务
                return operations.exec();
            }
        });
    }

    // 查询某实体获得的点赞数量用于详情页统计点赞数量
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);

    }
        //查询某人对某个实体有没有点赞
        // 查询某人对某实体的点赞状态
        public int findEntityLikeStatus ( int userId, int entityType, int entityId){
            String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
            return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
        }

        // 查询某个用户获得的赞
        public int findUserLikeCount(int userId){
            String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
            Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
            return count == null ? 0 : count.intValue();
        }


}

