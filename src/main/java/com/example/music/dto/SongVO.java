package com.example.music.dto;

import lombok.Data;

@Data
public class SongVO {
    private Object id;          // 本地歌曲为 Long，第三方为 String
    private String title;
    private String artist;
    private String album;
    private Integer duration;
    private String coverUrl;
    private String audioUrl;
    private String source;      // "local" 或 "tencent"
}