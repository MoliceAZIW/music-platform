package com.example.music.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("/page")
    public Map<String, Object> pageSong(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Song> page = songService.getSongPage(pageNum, pageSize, categoryId);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", page);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getSongById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Song song = songService.getSongById(id);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", song);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/search")
    public Map<String, Object> searchSong(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<Song> page = songService.searchSongs(keyword, pageNum, pageSize);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", page);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}