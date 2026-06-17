package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.music.entity.PlaylistSong;
import com.example.music.entity.Song;
import com.example.music.mapper.PlaylistSongMapper;
import com.example.music.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistSongService {
    @Autowired
    private PlaylistSongMapper playlistSongMapper;
    @Autowired
    private SongMapper songMapper;

    public boolean addSongToPlaylist(Long playlistId, Long songId) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId).eq("song_id", songId);
        if (playlistSongMapper.selectCount(wrapper) > 0) {
            return false;
        }
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setPlaylistId(playlistId);
        playlistSong.setSongId(songId);
        return playlistSongMapper.insert(playlistSong) > 0;
    }

    public boolean removeSongFromPlaylist(Long playlistId, Long songId) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId).eq("song_id", songId);
        return playlistSongMapper.delete(wrapper) > 0;
    }

    public List<Song> getSongsByPlaylistId(Long playlistId) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId);
        List<PlaylistSong> relations = playlistSongMapper.selectList(wrapper);
        List<Long> songIds = relations.stream()
                .map(PlaylistSong::getSongId)
                .collect(Collectors.toList());
        if (songIds.isEmpty()) {
            return List.of();
        }
        return songMapper.selectBatchIds(songIds);
    }

    public void deleteByPlaylistId(Long playlistId) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId);
        playlistSongMapper.delete(wrapper);
    }
}