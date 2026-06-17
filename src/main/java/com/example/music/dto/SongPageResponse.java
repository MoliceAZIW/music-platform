package com.example.music.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class SongPageResponse {
    private List<?> list;
    private Long total;
    private Long page;
    private Long pageSize;
}