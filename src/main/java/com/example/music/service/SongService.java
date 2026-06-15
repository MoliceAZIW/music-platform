package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {

    @Autowired
    private SongMapper songMapper;

    public Page<Song> getSongPage(Integer pageNum, Integer pageSize, Integer categoryId) {
        Page<Song> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Song> wrapper = new QueryWrapper<>();
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        return songMapper.selectPage(page, wrapper);
    }

    public Song getSongById(Long id) {
        return songMapper.selectById(id);
    }

    public Page<Song> searchSongs(String keyword, Integer pageNum, Integer pageSize) {
        Page<Song> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Song> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword)
                .or().like("singer", keyword)
                .or().like("lyricist", keyword);
        return songMapper.selectPage(page, wrapper);
    }
}