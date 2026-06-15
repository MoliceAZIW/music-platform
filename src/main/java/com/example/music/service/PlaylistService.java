package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.music.entity.Playlist;
import com.example.music.entity.PlaylistSong;
import com.example.music.mapper.PlaylistMapper;
import com.example.music.mapper.PlaylistSongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistSongMapper playlistSongMapper;

    public Playlist createPlaylist(Playlist playlist) {
        playlistMapper.insert(playlist);
        return playlist;
    }

    public List<Playlist> getUserPlaylists(Long userId) {
        QueryWrapper<Playlist> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return playlistMapper.selectList(wrapper);
    }

    @Transactional
    public boolean deletePlaylist(Long playlistId) {
        // 先删歌单-歌曲的关联数据
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId);
        playlistSongMapper.delete(wrapper);
        // 再删歌单本身
        return playlistMapper.deleteById(playlistId) > 0;
    }
}