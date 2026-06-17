package com.example.music.dto;

import lombok.Data;

@Data
public class PlaylistSongOperateRequest {
    private Long songId;
    private String action;
}