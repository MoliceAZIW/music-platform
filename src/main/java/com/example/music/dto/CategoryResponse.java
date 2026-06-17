package com.example.music.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private Integer sortOrder;
    private LocalDateTime createTime;
}