package com.example.music.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("playlist_song")
public class PlaylistSong {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long playlistId;
    private Long songId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;
}