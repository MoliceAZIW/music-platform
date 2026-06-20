CREATE DATABASE IF NOT EXISTS music_platform
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE music_platform;

-- 1. 用户表 (user)
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username`     varchar(50)  NOT NULL                COMMENT '登录账号',
                        `password`     varchar(255) NOT NULL                COMMENT 'BCrypt 加密密码',
                        `nickname`     varchar(50)  DEFAULT NULL            COMMENT '昵称',
                        `email`        varchar(100) DEFAULT NULL            COMMENT '邮箱',
                        `bio`          varchar(255) DEFAULT NULL            COMMENT '个人简介',
                        `gender`       tinyint      DEFAULT 0               COMMENT '0未知 1男 2女',
                        `hobby`        varchar(255) DEFAULT NULL            COMMENT '爱好',
                        `avatar`       varchar(255) DEFAULT NULL            COMMENT '头像 URL',
                        `create_time`  datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time`  datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_username` (`username`),
                        KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


-- 2. 分类表 (category)
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
                            `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
                            `name`         varchar(50)  NOT NULL                COMMENT '分类名称',
                            `description`  varchar(255) DEFAULT NULL            COMMENT '分类描述',
                            `icon`         varchar(255) DEFAULT NULL            COMMENT '图标 URL',
                            `sort_order`   int          DEFAULT 0               COMMENT '排序',
                            `create_time`  datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time`  datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌曲分类表';


-- 3. 歌曲表 (song)

DROP TABLE IF EXISTS `song`;
CREATE TABLE `song` (
                        `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '歌曲ID',
                        `name`            varchar(100) NOT NULL                COMMENT '歌曲名',
                        `lyricist`        varchar(50)  DEFAULT NULL            COMMENT '作词人',
                        `composer`        varchar(50)  DEFAULT NULL            COMMENT '作曲人',
                        `lyrics`          text                                 COMMENT '完整歌词',
                        `audio_url`       varchar(255) DEFAULT NULL            COMMENT '音频文件 URL',
                        `mv_url`          varchar(255) DEFAULT NULL            COMMENT 'MV 视频 URL',
                        `mv_description`  varchar(500) DEFAULT NULL            COMMENT 'MV 描述',
                        `mv_author`       varchar(100) DEFAULT NULL            COMMENT 'MV 作者',
                        `category_id`     bigint       DEFAULT NULL            COMMENT '分类ID',
                        `category`        varchar(50)  DEFAULT NULL            COMMENT '分类名称',
                        `cover_url`       varchar(255) DEFAULT NULL            COMMENT '封面图 URL',
                        `create_time`     datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        PRIMARY KEY (`id`),
                        KEY `idx_category_id` (`category_id`),
                        KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地歌曲表';


-- 4. 歌单表 (playlist)
DROP TABLE IF EXISTS `playlist`;
CREATE TABLE `playlist` (
                            `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '歌单ID',
                            `user_id`      bigint       NOT NULL                COMMENT '所属用户ID（外键 → user.id）',
                            `name`         varchar(100) NOT NULL                COMMENT '歌单名称',
                            `cover_url`    varchar(255) DEFAULT NULL            COMMENT '封面图 URL',
                            `description`  varchar(500) DEFAULT NULL            COMMENT '歌单描述',
                            `create_time`  datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            PRIMARY KEY (`id`),
                            KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单主表';



-- 5. 歌单-歌曲关联表 (playlist_song)
DROP TABLE IF EXISTS `playlist_song`;
CREATE TABLE `playlist_song` (
                                 `id`               bigint       NOT NULL AUTO_INCREMENT COMMENT '关联记录ID',
                                 `playlist_id`      bigint       NOT NULL                COMMENT '歌单ID',
                                 `song_id`          varchar(64)  NOT NULL                COMMENT '歌曲ID（本地为数字，第三方为字符串mid）',
                                 `source`           varchar(20)  DEFAULT 'local'         COMMENT '来源: local/tencent',
                                 `external_name`    varchar(200) DEFAULT NULL            COMMENT '第三方歌曲名（冗余）',
                                 `external_artist`  varchar(200) DEFAULT NULL            COMMENT '第三方歌手名（冗余）',
                                 `external_cover`   varchar(500) DEFAULT NULL            COMMENT '第三方封面图（冗余）',
                                 `external_vid`     varchar(50)  DEFAULT NULL            COMMENT '第三方 MV 的 vid（冗余）',
                                 `add_time`         datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_playlist_song` (`playlist_id`, `song_id`, `source`),
                                 KEY `idx_playlist_id` (`playlist_id`),
                                 KEY `idx_song_id` (`song_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='歌单-歌曲关联表';


-- 6. 初始化数据（测试用）

-- 6.1 插入测试用户
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `bio`, `gender`, `hobby`, `avatar`)
VALUES
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iK2xoXh2', '管理员', 'admin@music.com', '我是管理员', 1, '音乐/读书', '/avatar/admin.jpg'),
    ('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iK2xoXh2', '测试用户', 'test@music.com', '测试账号', 0, '听歌/打篮球', '/avatar/test.jpg');
-- 密码都是: 123456 （BCrypt 加密）

-- 6.2 插入分类数据
INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
                                                                 ('流行', '流行音乐', 1),
                                                                 ('摇滚', '摇滚音乐', 2),
                                                                 ('民谣', '民谣音乐', 3),
                                                                 ('电子', '电子音乐', 4),
                                                                 ('R&B', '节奏布鲁斯', 5),
                                                                 ('说唱', '嘻哈说唱', 6),
                                                                 ('古典', '古典音乐', 7);


-- 6.3 插入歌单
INSERT INTO `playlist` (`user_id`, `name`, `description`, `cover_url`) VALUES
                                                                           (1, '周董精选集', '周杰伦经典歌曲合集', '/cover/playlist_zhoudong.jpg'),
                                                                           (1, '治愈励志歌单', '听完充满力量', '/cover/playlist_cure.jpg');
