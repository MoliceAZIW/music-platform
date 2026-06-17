package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.music.dto.PlaylistCreateRequest;
import com.example.music.dto.PlaylistCreateResponse;
import com.example.music.dto.PlaylistDetailResponse;
import com.example.music.dto.PlaylistResponse;
import com.example.music.dto.SongSimpleResponse;
import com.example.music.entity.Playlist;
import com.example.music.entity.Song;
import com.example.music.mapper.PlaylistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistMapper playlistMapper;
    @Autowired
    private PlaylistSongService playlistSongService;

    public PlaylistCreateResponse createPlaylist(PlaylistCreateRequest dto, Long loginUserId) {
        Playlist playlist = new Playlist();
        playlist.setUserId(loginUserId);
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setCoverUrl(null);
        playlistMapper.insert(playlist);
        PlaylistCreateResponse resp = new PlaylistCreateResponse();
        resp.setId(playlist.getId());
        resp.setName(playlist.getName());
        resp.setCreatedAt(playlist.getCreateTime());
        return resp;
    }

    public List<PlaylistResponse> getUserPlaylists(Long loginUserId) {
        QueryWrapper<Playlist> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", loginUserId);
        List<Playlist> list = playlistMapper.selectList(wrapper);
        return list.stream().map(p -> {
            PlaylistResponse vo = new PlaylistResponse();
            vo.setId(p.getId());
            vo.setName(p.getName());
            vo.setCoverUrl(p.getCoverUrl());
            vo.setCreatedAt(p.getCreateTime());
            List<Song> songs = playlistSongService.getSongsByPlaylistId(p.getId());
            vo.setSongCount(songs.size());
            return vo;
        }).collect(Collectors.toList());
    }

    public PlaylistDetailResponse getPlaylistDetail(Long playlistId, Long loginUserId) {
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null || !playlist.getUserId().equals(loginUserId)) {
            return null;
        }
        PlaylistDetailResponse vo = new PlaylistDetailResponse();
        vo.setId(playlistId);
        vo.setName(playlist.getName());
        vo.setCoverUrl(playlist.getCoverUrl());
        vo.setDescription(playlist.getDescription());
        List<Song> songs = playlistSongService.getSongsByPlaylistId(playlistId);
        List<SongSimpleResponse> songVos = songs.stream().map(s -> {
            SongSimpleResponse sv = new SongSimpleResponse();
            sv.setId(s.getId());
            sv.setTitle(s.getTitle());
            sv.setArtist(s.getMvAuthor());
            sv.setAudioUrl(s.getAudioUrl());
            sv.setCoverUrl(s.getCoverUrl());
            return sv;
        }).collect(Collectors.toList());
        vo.setSongs(songVos);
        return vo;
    }

    @Transactional
    public boolean deletePlaylist(Long playlistId) {
        playlistSongService.deleteByPlaylistId(playlistId);
        return playlistMapper.deleteById(playlistId) > 0;
    }
}