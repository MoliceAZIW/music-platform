package com.example.music.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlaylistDetailResponse {
    private Long id;
    private String name;
    private String coverUrl;
    private String description;
    private List<SongSimpleResponse> songs;
}