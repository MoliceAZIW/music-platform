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

INSERT INTO `song`
(`name`, lyricist, composer, lyrics, audio_url, mv_url, mv_description, mv_author, category, cover_url)
VALUES
    ('晴天', '周杰伦', '周杰伦', '故事的小黄花，从出生那年就飘着', '/audio/qt.mp3', '/mv/qt.mp4', '校园情歌MV', '周杰伦', '流行', '/cover/qt.jpg'),
    ('稻香', '周杰伦', '周杰伦', '还记得家是唯一的城堡', '/audio/dx.mp3', '/mv/dx.mp4', '治愈乡村MV', '周杰伦', '治愈', '/cover/dx.jpg'),
    ('孤勇者', '唐恬', '钱雷', '爱你孤身走暗巷，爱你不跪的模样', '/audio/gyz.mp3', '/mv/gyz.mp4', '动画双城之战主题曲', '陈奕迅', '励志', '/cover/gyz.jpg'),
    ('七里香', '方文山', '周杰伦', '窗外的麻雀，在电线杆上多嘴', '/audio/qlx.mp3', NULL, NULL, '周杰伦', '流行', '/cover/qlx.jpg');

INSERT INTO `playlist` (user_id, `name`)
VALUES
    (1, '周董精选集'),
    (1, '治愈励志歌单');

CREATE TABLE IF NOT EXISTS `category` (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `name` varchar(50) NOT NULL COMMENT '分类名称',
    `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
    `icon` varchar(255) DEFAULT NULL COMMENT '图标URL',
    `sort_order` int DEFAULT '0' COMMENT '排序',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌曲分类表';

ALTER TABLE `song` ADD COLUMN `category_id` bigint DEFAULT NULL COMMENT '分类ID';
ALTER TABLE `song` ADD INDEX `idx_category_id` (`category_id`);

INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
                                                                 ('流行', '流行音乐', 1),
                                                                 ('摇滚', '摇滚音乐', 2),
                                                                 ('民谣', '民谣音乐', 3),
                                                                 ('电子', '电子音乐', 4),
                                                                 ('R&B', '节奏布鲁斯', 5),
                                                                 ('说唱', '嘻哈说唱', 6),
                                                                 ('古典', '古典音乐', 7);

SELECT * FROM song WHERE name LIKE '%周杰伦%' OR lyricist LIKE '%周杰伦%';

UPDATE `song` SET `category_id` = 1 WHERE `id` IN (1, 4);
UPDATE `song` SET `category_id` = 2 WHERE `id` = 2;

ALTER TABLE `playlist` ADD COLUMN `cover_url` varchar(255) DEFAULT NULL;
ALTER TABLE `playlist` ADD COLUMN `description` varchar(500) DEFAULT NULL;

INSERT INTO playlist_song (playlist_id, song_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);
INSERT INTO playlist_song (playlist_id, song_id) VALUES (2,1),(2,3);
INSERT INTO playlist_song (playlist_id, song_id) VALUES (3,2),(3,4);
INSERT INTO playlist_song (playlist_id, song_id) VALUES (1, 5);
DELETE FROM playlist_song WHERE playlist_id = 1 AND song_id = 4;