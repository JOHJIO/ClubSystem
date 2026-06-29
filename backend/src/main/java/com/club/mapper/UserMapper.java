package com.club.mapper;

import com.club.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}