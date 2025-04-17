package com.quinn.tenement.service;

import com.quinn.tenement.entity.User;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
public interface UserService {
    User register(User user);
    User login(String username, String password);
    User findByUsername(String username);
    User findById(Long id);
}