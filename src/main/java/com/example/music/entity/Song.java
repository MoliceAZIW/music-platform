package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("song")
public class Song {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("name")
    private String title;
    @TableField(exist = false)
    private String artist;
    @TableField(exist = false)
    private String album;
    @TableField(exist = false)
    private Integer duration;
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