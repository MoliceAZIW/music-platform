package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.*;
import com.example.music.entity.User;
import com.example.music.service.UserService;
import com.example.music.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 注册
    @PostMapping("/auth/register")
    public Result<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        try {
            User user = userService.register(request.getUsername(), request.getPassword(), request.getEmail());
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            return Result.success(new AuthResponse(user.getId(), user.getUsername(), token));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    // 登录
    @PostMapping("/auth/login")
    public Result<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            return Result.success(new AuthResponse(user.getId(), user.getUsername(), token));
        } catch (RuntimeException e) {
            return Result.error(401, e.getMessage());
        }
    }

    // 获取个人信息
    @GetMapping("/user/profile")
    public Result<UserProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getUserById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(user, response);
        if (user.getCreateTime() != null) {
            response.setCreateTime(user.getCreateTime().toString());
        }
        return Result.success(response);
    }

    // 更新个人信息
    @PutMapping("/user/profile")
    public Result<UserProfileResponse> updateProfile(@RequestHeader("Authorization") String authHeader,
                                                     @RequestBody UserUpdateRequest request) {
        System.out.println("updateProfile 方法被调用了");
        // 1. 校验 token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }
        // 2. 从 token 中获取 userId
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 3. 调用 Service 更新
        User updated = userService.updateUser(userId, request);

        // 4. 转换为响应 DTO
        UserProfileResponse response = new UserProfileResponse();
        BeanUtils.copyProperties(updated, response);
        if (updated.getCreateTime() != null) {
            response.setCreateTime(updated.getCreateTime().toString());
        }
        return Result.success(response);
    }

    // 修改密码
    @PostMapping("/user/password")
    public Result<Void> updatePassword(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody PasswordUpdateRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录");
        }
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return Result.error(401, "Token无效或已过期");
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        try {
            userService.updatePassword(userId, request.getOldPassword(), request.getNewPassword());
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}