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
    private String songId;
    private String source;         //资源来源
    private String externalName;
    private String externalArtist;
    private String externalCover;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;
    private String externalVid;
}