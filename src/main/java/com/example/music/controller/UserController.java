package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.*;
import com.example.music.entity.User;
import com.example.music.service.UserService;
import com.example.music.utils.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER_ERROR = "未登录或Token无效";

    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 构造器注入
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 注册
    @PostMapping("/auth/register")
    public Result<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        log.info("用户注册：{}", request.getUsername());
        User user = userService.register(request.getUsername(), request.getPassword(), request.getEmail());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return Result.success(new AuthResponse(user.getId(), user.getUsername(), token));
    }

    // 登录
    @PostMapping("/auth/login")
    public Result<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("用户登录：{}", request.getUsername());
        User user = userService.login(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return Result.success(new AuthResponse(user.getId(), user.getUsername(), token));
    }

    // 获取个人信息
    @GetMapping("/user/profile")
    public Result<UserProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        Long userId = validateAndGetUserId(authHeader);
        User user = userService.getUserById(userId);
        if (user == null) {
            log.warn("用户不存在，userId={}", userId);
            throw new RuntimeException("用户不存在"); // 由全局异常处理器处理
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
                                                     @Valid @RequestBody UserUpdateRequest request) {
        Long userId = validateAndGetUserId(authHeader);
        log.info("更新用户信息，userId={}", userId);
        User updated = userService.updateUser(userId, request);
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
                                       @Valid @RequestBody PasswordUpdateRequest request) {
        Long userId = validateAndGetUserId(authHeader);
        log.info("修改密码，userId={}", userId);
        userService.updatePassword(userId, request.getOldPassword(), request.getNewPassword());
        return Result.success(null);
    }

    // 私有辅助方法
    private Long validateAndGetUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            log.warn("Authorization头缺失或格式错误");
            throw new RuntimeException(AUTH_HEADER_ERROR); // 全局异常处理返回401
        }
        String token = authHeader.substring(BEARER_PREFIX.length());
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token无效或已过期");
            throw new RuntimeException(AUTH_HEADER_ERROR);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}