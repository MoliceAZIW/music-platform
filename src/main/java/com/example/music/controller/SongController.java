package com.example.music.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("")
    public Map<String, Object> pageSong(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String keyword
    ) {
        Map<String, Object> result = new HashMap<>();
        Page<Song> pageResult = songService.getSongPage(page, pageSize, keyword);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        data.put("page", pageResult.getCurrent());
        data.put("pageSize", pageResult.getSize());
        result.put("data", data);
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getSongById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", songService.getSongById(id));
        return result;
    }

    @GetMapping("/search")
    public Map<String, Object> searchSong(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "song") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        Map<String, Object> result = new HashMap<>();
        Page<Song> pageResult = songService.searchSongs(keyword, page, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getRecords());
        data.put("total", pageResult.getTotal());
        data.put("page", pageResult.getCurrent());
        data.put("pageSize", pageResult.getSize());
        result.put("data", data);
        return result;
    }
}