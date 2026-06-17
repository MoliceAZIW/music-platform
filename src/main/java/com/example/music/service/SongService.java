package com.example.music.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.music.entity.Song;
import com.example.music.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SongService {

    @Autowired
    private SongMapper songMapper;

    public Page<Song> getSongPage(Integer page, Integer pageSize, String keyword, String type,Long categoryId)  {
        LambdaQueryWrapper<Song> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            // 根据类型构建不同的查询条件
            if ("artist".equals(type)) {
                wrapper.like(Song::getLyricist, keyword);
            } else if ("album".equals(type)) {
                wrapper.like(Song::getAlbum, keyword);
            } else {
                // 默认搜索歌名和作词
                wrapper.and(w -> w.like(Song::getTitle, keyword)
                        .or()
                        .like(Song::getLyricist, keyword));
            }
        }
        // 分类筛选
        if (categoryId != null) {
            wrapper.eq(Song::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Song::getCreateTime);

        Page<Song> pageResult = new Page<>(page, pageSize);
        return songMapper.selectPage(pageResult, wrapper);
    }

    public Song getSongById(Long id) {
        return songMapper.selectById(id);
    }

}