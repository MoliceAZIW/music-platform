package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("song")
public class Song {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String lyricist;
    private String composer;
    private String lyrics;
    private String audioUrl;
    private String mvUrl;
    private String mvDescription;
    private String mvAuthor;
    private String category;
    private String coverUrl;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
