package com.example.music.service;

import com.example.music.dto.PlaylistDetailVO;
import com.example.music.dto.SongVO;
import com.example.music.entity.Playlist;
import com.example.music.entity.PlaylistSong;
import com.example.music.mapper.PlaylistMapper;
import com.example.music.mapper.PlaylistSongMapper;
import com.example.music.mapper.SongMapper;
import com.example.music.service.thirdparty.TencentMusicService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    private PlaylistMapper playlistMapper;

    @Mock
    private PlaylistSongMapper playlistSongMapper;

    @Mock
    private SongMapper songMapper;

    @Mock
    private TencentMusicService tencentMusicService;

    @InjectMocks
    private PlaylistService playlistService;

    @Test
    void testGetPlaylistDetail_WithTencentSong_ShouldFillAudioUrl() {
        // 1. 模拟数据
        Long playlistId = 1L;
        Long userId = 1L;

        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);
        mockPlaylist.setUserId(userId);
        mockPlaylist.setName("我的歌单");

        PlaylistSong mockRelation = new PlaylistSong();
        mockRelation.setSource("tencent");
        mockRelation.setSongId("004AxZur0wQjFR");
        mockRelation.setExternalName("可惜没如果");
        mockRelation.setExternalArtist("林俊杰");
        mockRelation.setExternalVid("d0015z6s2c7");

        // 2. Mock 行为 —— ⚠️ 注意这里要传两个参数！
        when(playlistMapper.selectById(playlistId)).thenReturn(mockPlaylist);
        when(playlistSongMapper.selectByPlaylistId(playlistId)).thenReturn(List.of(mockRelation));
        when(tencentMusicService.getPlayUrl("004AxZur0wQjFR", 10))
                .thenReturn("http://test.mp3");  // ✅ 两个参数，用逗号分隔

        // 3. 执行测试
        PlaylistDetailVO result = playlistService.getPlaylistDetail(playlistId, userId);

        // 4. 断言
        assertNotNull(result);
        assertEquals("我的歌单", result.getName());
        assertEquals(1, result.getSongs().size());

        SongVO song = result.getSongs().get(0);
        assertEquals("可惜没如果", song.getTitle());
        assertEquals("tencent", song.getSource());
        assertEquals("http://test.mp3", song.getAudioUrl());
        assertEquals("d0015z6s2c7", song.getVid());

        // 5. 验证第三方 API 确实被调用了（两个参数）
        verify(tencentMusicService, times(1)).getPlayUrl("004AxZur0wQjFR", 10);
    }

    @Test
    void testGetPlaylistDetail_WithTencentSong_ShouldFallbackWhenHighQualityFails() {
        // 测试降级逻辑：quality=10 失败，降级到 quality=7
        Long playlistId = 1L;
        Long userId = 1L;

        Playlist mockPlaylist = new Playlist();
        mockPlaylist.setId(playlistId);
        mockPlaylist.setUserId(userId);

        PlaylistSong mockRelation = new PlaylistSong();
        mockRelation.setSource("tencent");
        mockRelation.setSongId("004AxZur0wQjFR");
        mockRelation.setExternalName("可惜没如果");
        mockRelation.setExternalArtist("林俊杰");

        when(playlistMapper.selectById(playlistId)).thenReturn(mockPlaylist);
        when(playlistSongMapper.selectByPlaylistId(playlistId)).thenReturn(List.of(mockRelation));

        // quality=10 返回 null（失败）
        when(tencentMusicService.getPlayUrl("004AxZur0wQjFR", 10))
                .thenReturn(null);
        // quality=7 返回有效链接（降级成功）
        when(tencentMusicService.getPlayUrl("004AxZur0wQjFR", 7))
                .thenReturn("http://fallback.mp3");

        PlaylistDetailVO result = playlistService.getPlaylistDetail(playlistId, userId);

        SongVO song = result.getSongs().get(0);
        assertEquals("http://fallback.mp3", song.getAudioUrl());  // 应使用降级后的链接

        // 验证两个质量都被调用了
        verify(tencentMusicService, times(1)).getPlayUrl("004AxZur0wQjFR", 10);
        verify(tencentMusicService, times(1)).getPlayUrl("004AxZur0wQjFR", 7);
    }
}