package com.example.music.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaylistResponse {
    private Long id;
    private String name;
    private String coverUrl;
    private Integer songCount;
    private LocalDateTime createdAt;
}