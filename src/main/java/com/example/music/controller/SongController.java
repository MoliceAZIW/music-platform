package com.example.music.controller;

import com.example.music.common.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.service.SongService;
import com.example.music.dto.SongPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping
    public Result<SongPageResponse> listSongs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "song") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Long categoryId
    ) {
        Page<Song> pageResult = songService.getSongPage(page, pageSize, keyword, type,categoryId);
        SongPageResponse response = new SongPageResponse(
                pageResult.getRecords(),
                pageResult.getTotal(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
        return Result.success(response);
    }

    @GetMapping("/search")
    public Result<SongPageResponse> searchSongs(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "song") String type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Long categoryId
    ) {
        return listSongs(keyword, type, page, pageSize,categoryId);
    }


    @GetMapping("/{id}")
    public Result<Song> getSongById(@PathVariable Long id) {
        return Result.success(songService.getSongById(id));
    }
}