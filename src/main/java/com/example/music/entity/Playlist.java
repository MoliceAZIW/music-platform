package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("playlist")
public class Playlist {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String name;
    private String coverUrl;
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}