package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.music.service.PlaylistSongService;
import com.example.music.dto.*;
import com.example.music.entity.Playlist;
import com.example.music.entity.PlaylistSong;
import com.example.music.entity.Song;
import com.example.music.mapper.PlaylistMapper;
import com.example.music.mapper.PlaylistSongMapper;
import com.example.music.mapper.SongMapper;
import com.example.music.service.thirdparty.TencentMusicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PlaylistService {
    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private PlaylistSongService playlistSongService;

    @Autowired
    private PlaylistSongMapper playlistSongMapper;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private TencentMusicService tencentMusicService;

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
            QueryWrapper<PlaylistSong> countWrapper = new QueryWrapper<>();
            countWrapper.eq("playlist_id", p.getId());
            int count = playlistSongMapper.selectCount(countWrapper).intValue();
            vo.setSongCount(count);
            return vo;
        }).collect(Collectors.toList());
    }

    // PlaylistService.java 核心逻辑
    public PlaylistDetailVO getPlaylistDetail(Long playlistId, Long userId) {
        //  检查歌单归属
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null || !playlist.getUserId().equals(userId)) {
            throw new RuntimeException("歌单不存在或无权限");
        }

        // 查询关联表
        List<PlaylistSong> relations = playlistSongMapper.selectByPlaylistId(playlistId);

        List<SongVO> songList = new ArrayList<>();
        for (PlaylistSong relation : relations) {
            SongVO vo = new SongVO();
            if ("local".equals(relation.getSource())) {
                // 本地歌曲
                Long localId = Long.parseLong(relation.getSongId());
                Song localSong = songMapper.selectById(localId);
                if (localSong != null) {
                    BeanUtils.copyProperties(localSong, vo);
                    vo.setSource("local");
                    vo.setId(localSong.getId());     // Long 类型
                } else {
                    continue;
                }
            } else if ("tencent".equals(relation.getSource())) {
                // 第三方歌曲
                String mid = relation.getSongId();
                vo.setId(mid);   // String 类型
                vo.setSource("tencent");
                vo.setTitle(relation.getExternalName());
                vo.setArtist(relation.getExternalArtist());
                vo.setCoverUrl(relation.getExternalCover());
                // 实时获取播放链接
                String playUrl = tencentMusicService.getPlayUrl(mid, 10);
                if (playUrl == null) {
                    playUrl = tencentMusicService.getPlayUrl(mid, 7);
                }
                vo.setAudioUrl(playUrl);
            }
            // 其他来源暂不处理
            if (vo.getTitle() != null) {
                songList.add(vo);
            }
        }

        return PlaylistDetailVO.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .coverUrl(playlist.getCoverUrl())
                .description(playlist.getDescription())
                .songs(songList)
                .build();
    }

    // 添加第三方歌曲到歌单
    public boolean addExternalSongToPlaylist(Long playlistId, String songId, String source,
                                             String name, String artist, String cover) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId)
                .eq("song_id", songId)
                .eq("source", source);
        if (playlistSongMapper.selectCount(wrapper) > 0) {
            return false; // 已存在
        }
        PlaylistSong ps = new PlaylistSong();
        ps.setPlaylistId(playlistId);
        ps.setSongId(songId);
        ps.setSource(source);
        ps.setExternalName(name);
        ps.setExternalArtist(artist);
        ps.setExternalCover(cover);
        return playlistSongMapper.insert(ps) > 0;
    }

    // 移除第三方歌曲
    public boolean removeExternalSongFromPlaylist(Long playlistId, String songId, String source) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId)
                .eq("song_id", songId)
                .eq("source", source);
        return playlistSongMapper.delete(wrapper) > 0;
    }

    @Transactional
    public boolean deletePlaylist(Long playlistId) {
        QueryWrapper<PlaylistSong> wrapper = new QueryWrapper<>();
        wrapper.eq("playlist_id", playlistId);
        playlistSongMapper.delete(wrapper);
        return playlistMapper.deleteById(playlistId) > 0;
    }
}