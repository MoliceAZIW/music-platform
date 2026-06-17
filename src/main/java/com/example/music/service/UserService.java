package com.example.music.service;

import com.example.music.dto.UserUpdateRequest;
import com.example.music.entity.User;
import com.example.music.dto.RegisterRequest;

public interface UserService {
    User register(RegisterRequest request);
    User login(String username, String password);
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateRequest request);
    void updatePassword(Long userId, String oldPassword, String newPassword);
}