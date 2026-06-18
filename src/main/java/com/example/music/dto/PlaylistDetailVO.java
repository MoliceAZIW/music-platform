package com.example.music.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PlaylistDetailVO {
    private Long id;
    private String name;
    private String coverUrl;
    private String description;
    private List<SongVO> songs;
}