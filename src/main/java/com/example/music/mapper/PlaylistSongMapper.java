package com.example.music.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.music.entity.PlaylistSong;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface PlaylistSongMapper extends BaseMapper<PlaylistSong> {

    @Select("SELECT * FROM playlist_song WHERE playlist_id = #{playlistId}")
    List<PlaylistSong> selectByPlaylistId(Long playlistId);
}