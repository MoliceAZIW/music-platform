package com.example.music.controller;

import com.example.music.entity.Playlist;
import com.example.music.entity.Song;
import com.example.music.service.PlaylistService;
import com.example.music.service.PlaylistSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistSongService playlistSongService;

    @PostMapping
    public Map<String, Object> createPlaylist(@RequestBody Playlist playlist) {
        Map<String, Object> result = new HashMap<>();
        try {
            Playlist created = playlistService.createPlaylist(playlist);
            result.put("success", true);
            result.put("message", "创建成功");
            result.put("data", created);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getUserPlaylists(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Playlist> playlists = playlistService.getUserPlaylists(userId);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", playlists);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/{playlistId}")
    public Map<String, Object> deletePlaylist(@PathVariable Long playlistId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = playlistService.deletePlaylist(playlistId);
            result.put("success", deleted);
            result.put("message", deleted ? "删除成功" : "删除失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/addSong")
    public Map<String, Object> addSongToPlaylist(
            @RequestParam Long playlistId,
            @RequestParam Long songId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean added = playlistSongService.addSongToPlaylist(playlistId, songId);
            result.put("success", added);
            result.put("message", added ? "添加成功" : "歌曲已在歌单中");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @DeleteMapping("/removeSong")
    public Map<String, Object> removeSongFromPlaylist(
            @RequestParam Long playlistId,
            @RequestParam Long songId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean removed = playlistSongService.removeSongFromPlaylist(playlistId, songId);
            result.put("success", removed);
            result.put("message", removed ? "移除成功" : "移除失败");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{playlistId}/songs")
    public Map<String, Object> getSongsByPlaylistId(@PathVariable Long playlistId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Song> songs = playlistSongService.getSongsByPlaylistId(playlistId);
            result.put("success", true);
            result.put("message", "查询成功");
            result.put("data", songs);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}