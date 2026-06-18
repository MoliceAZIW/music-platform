package com.example.music.service.thirdparty;

import com.example.music.dto.thirdparty.TencentLyricDTO;
import com.example.music.dto.thirdparty.TencentSongDTO;
import java.util.List;

public interface TencentMusicService {
    List<TencentSongDTO> searchSongs(String keyword, int limit);
    String getPlayUrl(String mid, int quality);
    TencentLyricDTO getLyric(String mid);
}