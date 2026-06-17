package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.PlaylistCreateRequest;
import com.example.music.dto.PlaylistCreateResponse;
import com.example.music.dto.PlaylistDetailResponse;
import com.example.music.dto.PlaylistResponse;
import com.example.music.dto.PlaylistSongOperateRequest;
import com.example.music.service.PlaylistService;
import com.example.music.service.PlaylistSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    @Autowired
    private PlaylistService playlistService;
    @Autowired
    private PlaylistSongService playlistSongService;

    @GetMapping
    public Result<List<PlaylistResponse>> getUserPlaylists() {
        Long loginUserId = 1L;
        List<PlaylistResponse> voList = playlistService.getUserPlaylists(loginUserId);
        Result<List<PlaylistResponse>> result = Result.success(voList);
        result.setMessage("查询成功");
        return result;
    }

    @GetMapping("/{id}")
    public Result<PlaylistDetailResponse> getPlaylistDetail(@PathVariable Long id) {
        Long loginUserId = 1L;
        PlaylistDetailResponse detail = playlistService.getPlaylistDetail(id, loginUserId);
        if (detail == null) {
            return Result.error(404, "歌单不存在或无权限访问");
        }
        Result<PlaylistDetailResponse> result = Result.success(detail);
        result.setMessage("查询成功");
        return result;
    }

    @PostMapping
    public Result<PlaylistCreateResponse> createPlaylist(@RequestBody PlaylistCreateRequest dto) {
        try {
            Long loginUserId = 1L;
            PlaylistCreateResponse resp = playlistService.createPlaylist(dto, loginUserId);
            Result<PlaylistCreateResponse> result = Result.success(resp);
            result.setMessage("创建成功");
            return result;
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @PostMapping("/{id}/songs")
    public Result<?> operatePlaylistSong(@PathVariable Long id, @RequestBody PlaylistSongOperateRequest dto) {
        try {
            boolean flag;
            if ("add".equals(dto.getAction())) {
                flag = playlistSongService.addSongToPlaylist(id, dto.getSongId());
                if (!flag) {
                    return Result.error(400, "歌曲已存在于歌单");
                }
            } else if ("remove".equals(dto.getAction())) {
                flag = playlistSongService.removeSongFromPlaylist(id, dto.getSongId());
                if (!flag) {
                    return Result.error(400, "歌单内无该歌曲");
                }
            } else {
                return Result.error(400, "action参数仅支持add/remove");
            }
            Result<?> result = Result.success(Map.of("success", true));
            result.setMessage("操作成功");
            return result;
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> deletePlaylist(@PathVariable Long id) {
        try {
            boolean del = playlistService.deletePlaylist(id);
            if (!del) {
                return Result.error(404, "歌单不存在");
            }
            Result<?> result = Result.success(Map.of("success", true));
            result.setMessage("删除成功");
            return result;
        } catch (Exception e) {
            return Result.error(500, e.getMessage());
        }
    }
}