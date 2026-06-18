package com.example.music.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class PlaylistSongAddRequest {
    @NotNull(message = "歌曲ID不能为空")
    private String songId;          // 本地歌曲id 或 第三方mid
    private String source = "local"; // local / tencent
    private String name;            // 第三方歌曲名（冗余）
    private String artist;          // 第三方歌手（冗余）
    private String cover;           // 第三方封面（冗余）
}