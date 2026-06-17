package com.example.music.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlaylistCreateResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}