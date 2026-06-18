package com.example.music.service.thirdparty;

import com.example.music.dto.thirdparty.TencentLyricDTO;
import com.example.music.dto.thirdparty.TencentSongDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class TencentMusicServiceImpl implements TencentMusicService {

    private static final String VKEYS_BASE = "https://api.vkeys.cn/v2/music/tencent";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 统一发送 GET 请求的方法
    private String sendGetRequest(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        conn.setRequestProperty("Accept", "application/json, text/plain, */*");
        conn.setConnectTimeout(15000);
        conn.setReadTimeout(15000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HTTP 请求失败，状态码: " + responseCode);
        }

        try (InputStream is = conn.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } finally {
            conn.disconnect();
        }
    }

    @Override
    public List<TencentSongDTO> searchSongs(String keyword, int limit) {
        try {
            String url = VKEYS_BASE + "/search/song?word=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) + "&num=" + limit;
            String response = sendGetRequest(url);

            System.out.println("搜索响应: " + response); // 调试

            JsonNode root = objectMapper.readTree(response);
            if (root.path("code").asInt() != 200) {
                System.err.println("第三方返回错误码: " + root.path("code").asInt());
                return new ArrayList<>();
            }

            JsonNode dataArray = root.path("data");
            List<TencentSongDTO> result = new ArrayList<>();
            for (JsonNode item : dataArray) {
                TencentSongDTO dto = new TencentSongDTO();
                dto.setId(item.path("mid").asText());
                dto.setSong(item.path("song").asText());
                dto.setSinger(item.path("singer").asText());
                dto.setAlbum(item.path("album").asText());
                dto.setCover(item.path("cover").asText());
                dto.setInterval(item.path("interval").asText());
                dto.setVid(item.path("vid").asText());
                dto.setPay(item.path("pay").asText());
                result.add(dto);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public String getPlayUrl(String mid, int quality) {
        try {
            String url = VKEYS_BASE + "?mid=" + mid + "&quality=" + quality;
            String response = sendGetRequest(url);
            System.out.println("播放链接响应: " + response);

            JsonNode root = objectMapper.readTree(response);
            if (root.path("code").asInt() != 200) {
                return null;
            }
            return root.path("data").path("url").asText(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TencentLyricDTO getLyric(String mid) {
        try {
            String url = VKEYS_BASE + "/lyric?mid=" + mid;
            String response = sendGetRequest(url);
            System.out.println("歌词响应: " + response);

            JsonNode root = objectMapper.readTree(response);
            if (root.path("code").asInt() != 200) {
                return null;
            }
            JsonNode data = root.path("data");
            TencentLyricDTO lyric = new TencentLyricDTO();
            lyric.setLrc(data.path("lrc").asText());
            lyric.setTrans(data.path("trans").asText());
            return lyric;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getMvUrl(String vid) {
        // 直接返回 QQ 音乐官方 MV 页面地址
        return "https://y.qq.com/n/ryqq/mv/" + vid;
    }
}