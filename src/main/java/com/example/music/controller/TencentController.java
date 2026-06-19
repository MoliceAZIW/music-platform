package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.thirdparty.TencentLyricDTO;
import com.example.music.dto.thirdparty.TencentSongDTO;
import com.example.music.service.thirdparty.TencentMusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/thirdparty")
public class TencentController {

    @Autowired
    private TencentMusicService tencentMusicService;

    @GetMapping("/search")
    public Result<Map<String, Object>> search(@RequestParam String keyword,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "20") Integer num) {
        // 1. 一次性拉取 page * num 条数据（保证当前页数据够用）
        int limit = page * num;
        List<TencentSongDTO> allList = tencentMusicService.searchSongs(keyword, limit);

        // 2. 计算总条数（这里有个问题：第三方没有返回总数，怎么处理？见下面说明）
        long total = allList.size(); // 实际是 "已拉取到的总数"，而非真实总条数

        // 3. 计算当前页的起始位置
        int start = (page - 1) * num;
        int end = Math.min(start + num, allList.size());

        // 4. 截取当前页数据
        List<TencentSongDTO> pageList = allList.subList(start, end);

        // 5. 组装返回
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageList);
        result.put("total", total);
        result.put("page", page);
        result.put("num", num);

        return Result.success(result);
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

    @GetMapping("/mv/url")
    public Result<String> getMvUrl(@RequestParam String vid) {
        String url = tencentMusicService.getMvUrl(vid);
        if (url == null || url.isEmpty()) {
            return Result.error(404, "MV 地址不存在");
        }
        return Result.success(url);
    }
}