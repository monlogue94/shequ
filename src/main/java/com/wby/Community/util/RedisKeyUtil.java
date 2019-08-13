package com.wby.Community.util;
//key 是冒号隔开一个一个英文单词
public class RedisKeyUtil {
    private static final String   SPLIT =":";
    private static final String  PREFIX_ENTITY_LIKE="like:entity";
    private static final String  PREFIX_USER_LIKE="like:entity";


    public  static  String  getEntityLikeKey(int entityType,int entityId){
        return  PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }


    public  static  String  getUserLikeKey(int userId){
        return  PREFIX_USER_LIKE+SPLIT+SPLIT+userId ;
    }


}
