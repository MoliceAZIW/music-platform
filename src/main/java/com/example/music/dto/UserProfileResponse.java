package com.example.music.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String bio;
    private Integer gender;
    private String hobby;
    private String avatar;
    private String createTime;
}