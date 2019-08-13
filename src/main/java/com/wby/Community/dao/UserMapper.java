package com.wby.Community.dao;

import com.wby.Community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);//更新头像

    int updatePassword(int id, String password);
    //需要一个配置文件，给每一个方法提供sql，mybaits帮我们提供实现类







}
