package com.example.music.controller;

import com.example.music.entity.User;
import com.example.music.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    @GetMapping("/db")
    public Map<String, Object> testDb() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = userMapper.selectList(null);
            result.put("success", true);
            result.put("message", "数据库连接成功");
            result.put("userCount", users.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}