package com.wby.Community.util;
//key 是冒号隔开一个一个英文单词
public class RedisKeyUtil {
    private static final String   SPLIT =":";
    private static final String  PREFIX_ENTITY_LIKE="like:entity";
    private static final String  PREFIX_USER_LIKE="like:entity";
    private static final String  PREFIX_FOLLOWEE="followee";
    private static final String  PREFIX_FOLLOWER="follower";

//实体的赞
    public  static  String  getEntityLikeKey(int entityType,int entityId){
        return  PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

//用户的赞
    public  static  String  getUserLikeKey(int userId){
        return  PREFIX_USER_LIKE+SPLIT+SPLIT+userId ;
    }

    // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

}



