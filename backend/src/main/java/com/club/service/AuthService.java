package com.club.service;

import com.club.entity.User;
import com.club.exception.BusinessException;
import com.club.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    public User login(String username, String password) {
        User user = userMapper.selectByUsernameAndPassword(username, password);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        user.setPassword(null);
        return user;
    }
}