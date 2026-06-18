package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.thirdparty.TencentLyricDTO;
import com.example.music.dto.thirdparty.TencentSongDTO;
import com.example.music.service.thirdparty.TencentMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/thirdparty")
public class TencentController {

    @Autowired
    private TencentMusicService tencentMusicService;

    @GetMapping("/search")
    public Result<List<TencentSongDTO>> search(@RequestParam String keyword,
                                               @RequestParam(defaultValue = "20") int limit) {
        List<TencentSongDTO> list = tencentMusicService.searchSongs(keyword, limit);
        return Result.success(list);
    }

    @GetMapping("/playurl")
    public Result<String> getPlayUrl(@RequestParam String mid,
                                     @RequestParam(defaultValue = "10") int quality) {
        String url = tencentMusicService.getPlayUrl(mid, quality);
        if (url == null) {
            return Result.error(404, "获取播放链接失败，请尝试切换音质");
        }
        return Result.success(url);
    }

    @GetMapping("/lyric")
    public Result<TencentLyricDTO> getLyric(@RequestParam String mid) {
        TencentLyricDTO lyric = tencentMusicService.getLyric(mid);
        if (lyric == null) {
            return Result.error(404, "获取歌词失败");
        }
        return Result.success(lyric);
    }
}