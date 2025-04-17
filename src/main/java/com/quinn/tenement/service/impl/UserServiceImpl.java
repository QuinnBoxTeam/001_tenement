package com.quinn.tenement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quinn.tenement.entity.User;
import com.quinn.tenement.mapper.UserMapper;
import com.quinn.tenement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }
        // 保存用户信息
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                   .eq("password", password);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
}