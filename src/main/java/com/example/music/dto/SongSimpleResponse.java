package com.example.music.dto;

import lombok.Data;

@Data
public class SongSimpleResponse {
    private Long id;
    private String title;
    private String artist;
    private String audioUrl;
    private String coverUrl;
}