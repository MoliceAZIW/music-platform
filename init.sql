CREATE DATABASE IF NOT EXISTS music_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE music_platform;

-- 用户表
CREATE TABLE `user` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `username` varchar(50) NOT NULL COMMENT '登录账号',
                        `password` varchar(255) NOT NULL COMMENT '加密密码',
                        `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
                        `gender` tinyint DEFAULT '0' COMMENT '0未知 1男 2女',
                        `hobby` varchar(255) DEFAULT NULL COMMENT '爱好',
                        `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌曲表
CREATE TABLE `song` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(100) NOT NULL,
                        `lyricist` varchar(50) DEFAULT NULL COMMENT '作词',
                        `composer` varchar(50) DEFAULT NULL COMMENT '作曲',
                        `lyrics` text COMMENT '歌词',
                        `audio_url` varchar(255) DEFAULT NULL COMMENT '音频地址',
                        `mv_url` varchar(255) DEFAULT NULL COMMENT 'MV视频地址',
                        `mv_description` varchar(500) DEFAULT NULL COMMENT 'MV描述',
                        `mv_author` varchar(100) DEFAULT NULL COMMENT 'MV作者',
                        `category` varchar(50) DEFAULT NULL COMMENT '分类: 流行/摇滚/民谣等',
                        `cover_url` varchar(255) DEFAULT NULL COMMENT '封面图',
                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌单表
CREATE TABLE `playlist` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `user_id` bigint NOT NULL COMMENT '所属用户',
                            `name` varchar(100) NOT NULL,
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 歌单-歌曲关联表
CREATE TABLE `playlist_song` (
                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                 `playlist_id` bigint NOT NULL,
                                 `song_id` bigint NOT NULL,
                                 `add_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_playlist_song` (`playlist_id`,`song_id`),
                                 KEY `idx_playlist_id` (`playlist_id`),
                                 KEY `idx_song_id` (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
