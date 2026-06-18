package com.example.music.dto.thirdparty;

import lombok.Data;

@Data
public class TencentSongDTO {
    private String id;          // 对应 mid
    private String song;        // 歌名
    private String singer;      // 歌手
    private String album;       // 专辑
    private String cover;       // 封面 URL
    private String interval;    // 时长
    private String vid;         // MV 的 vid
    private String pay;         // 付费标识
}