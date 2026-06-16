package com.example.music.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String nickname;
    private String email;
    private String bio;
    private Integer gender;
    private String hobby;
    private String avatar;
}