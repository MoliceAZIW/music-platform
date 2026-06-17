package com.example.music.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CategoryRequest {
    @NotBlank(message = "分类名称不能为空")
    private String name;
    private String description;
    private String icon;
    private Integer sortOrder;
}