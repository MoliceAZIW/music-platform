package com.example.music.dto.thirdparty;

import lombok.Data;

@Data
public class TencentLyricDTO {
    private String lrc;         // 歌词 LRC 文本
    private String trans;       // 翻译歌词
}