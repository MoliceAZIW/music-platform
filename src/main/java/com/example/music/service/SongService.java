package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongMapper songMapper;

    public Page<Song> getSongPage(Integer page, Integer pageSize, String keyword) {
        QueryWrapper<Song> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like("name", keyword)
                    .or().like("lyricist", keyword);
        }
        wrapper.orderByDesc("create_time");

        Long total = songMapper.selectCount(wrapper);
        long offset = (long) (page - 1) * pageSize;
        wrapper.last("LIMIT " + offset + "," + pageSize);
        List<Song> records = songMapper.selectList(wrapper);

        Page<Song> pageResult = new Page<>(page, pageSize);
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        return pageResult;
    }

    public Song getSongById(Long id) {
        return songMapper.selectById(id);
    }

    public Page<Song> searchSongs(String keyword, Integer page, Integer pageSize) {
        return getSongPage(page, pageSize, keyword);
    }
}