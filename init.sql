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

ALTER TABLE playlist_song MODIFY song_id VARCHAR(64);

ALTER TABLE playlist_song ADD COLUMN source VARCHAR(20) DEFAULT 'local' COMMENT 'local:本地, tencent:QQ音乐';

-- 增加第三方额外信息字段
ALTER TABLE playlist_song ADD COLUMN external_name VARCHAR(200) NULL;
ALTER TABLE playlist_song ADD COLUMN external_artist VARCHAR(200) NULL;
ALTER TABLE playlist_song ADD COLUMN external_cover VARCHAR(500) NULL;

INSERT INTO song(name,lyricist,composer,lyrics,audio_url,mv_url,mv_description,mv_author,category,cover_url)
VALUES
-- 1 周杰伦 美人鱼
('美人鱼','罗宇轩/黄婕熙','周杰伦',
 '维京航海 日记簿\n停留在甲板等日出\n瓶中信 被丢入\n关于美人鱼的记录\n中世纪 的秘密\n从此后被塞入了瓶盖\n千年来你似乎 为等谁而存在\n或许凄美 在暧昧\n海与夕阳之间金黄的一切\n海岸线 在起雾\n似乎是离别适合的季节\n雾散后 只看见\n长发的你出现在岸边\n为了爱 忘了危险\n美人鱼的眼泪\n是一个连伤心都透明的世界\n地平线的远方一轮满月\n童话般感觉\n让我爱上有你的黑夜\n无声的眼泪\n水族玻璃里你一次次的来回\n思念成了仅存的那一切\n缺氧的感觉\n维京航海 日记簿\n停留在甲板等日出\n瓶中信 被丢入\n关于美人鱼的记录\n中世纪 的秘密\n从此后被塞入了瓶盖\n千年来你似乎 为等谁而存在\n或许凄美 在暧昧\n海与夕阳之间金黄的一切\n海岸线 在起雾\n似乎是离别适合的季节\n雾散后 只看见\n长发的你出现在岸边\n为了爱 忘了危险\n美人鱼的眼泪\n是一个连伤心都透明的世界\n地平线的远方一轮满月\n童话般感觉\n让我爱上有你的黑夜\n无声的眼泪\n水族玻璃里你一次次的来回\n思念成了仅存的那一切\n缺氧的感觉',
 '/audio/zjl_meirenyu.mp3',NULL,NULL,NULL,'华语流行','/cover/cover01.jpg'),

-- 2 周杰伦 七里香
('七里香','方文山','周杰伦',
 '窗外的麻雀在电线杆上多嘴\n你说这一句很有夏天的感觉\n手中的铅笔在纸上来来回回\n我用几行字形容你是我的谁\n秋刀鱼的滋味猫跟你都想了解\n初恋的香味就这样被我们寻回\n那温暖的阳光像刚摘的鲜艳草莓\n你说你舍不得吃掉这一种感觉\n雨下整夜我的爱溢出就像雨水\n院子落叶跟我的思念厚厚一叠\n几句是非也无法将我的热情冷却\n你出现在我诗的每一页\n雨下整夜我的爱溢出就像雨水\n窗台蝴蝶像诗里纷飞的美丽章节\n我接着写\n把永远爱你写进诗的结尾\n你是我唯一想要的了解\n雨下整夜我的爱溢出就像雨水\n院子落叶跟我的思念厚厚一叠\n几句是非也无法将我的热情冷却\n你出现在我诗的每一页\n那饱满的稻穗幸福了这个季节\n而你的脸颊像田里熟透的番茄\n你突然对我说七里香的名字很美\n我此刻却只想亲吻你倔强的嘴\n雨下整夜我的爱溢出就像雨水\n院子落叶跟我的思念厚厚一叠\n几句是非也无法将我的热情冷却\n你出现在我诗的每一页\n窗台蝴蝶像诗里纷飞的美丽章节\n我接着写\n把永远爱你写进诗的结尾\n你是我唯一想要的了解',
 '/audio/zjl_qilixiang.mp3',NULL,NULL,NULL,'华语流行','/cover/cover02.jpg'),

-- 3 周杰伦 说好不哭
('说好不哭','方文山','周杰伦',
 '没有了联络 后来的生活\n我都是听别人说\n说你怎么了 说你怎么过\n放不下的人是我\n人多的时候 就待在角落\n就怕别人问起我\n你们怎么了 你低着头护着我\n连抱怨都没有\n电话开始躲 从不对我说\n不习惯一个人生活\n离开我以后 要我好好过\n怕打扰想自由的我\n都这个时候 你还在意着\n别人是怎么怎么看我的\n拼命解释着 不是我的错 是你要走\n眼看着你难过 挽留的话却没有说\n你会微笑放手 说好不哭让我走\n电话开始躲 从不对我说\n不习惯一个人生活\n离开我以后 要我好好过\n怕打扰想自由的我\n都这个时候 你还在意着\n别人是怎么怎么看我的\n拼命解释着 不是我的错 是你要走\n眼看着你难过 挽留的话却没有说\n你会微笑放手 说好不哭让我走\n你什么都没有 却还为我的梦加油\n心疼过了多久\n过了多久\n还在找理由等我',
 '/audio/zjl_shuobuhu.mp3',NULL,NULL,NULL,'华语伤感流行','/cover/cover03.jpg'),

-- 4 李发发 孤单的人孤单的冬
('孤单的人孤单的冬','李守俊','罗智鸿',
 '孤单的人过着孤单的冬\n孤单的心想你发了疯\n孤单的我伴着孤单的灯\n孤单的梦最终一场空\n风那么冷雪那么凶\n那么爱你可是你不懂\n夜那么深黑那么重\n那么想你也都没有用\n爱那么真情那么浓\n那么伤害为何要相逢\n伤那么重心那么痛\n那么爱你也没让我赢\n孤单的人过着孤单的冬\n孤单的心想你发了疯\n孤单的我伴着孤单的灯\n孤单的梦最终一场空\n孤单的夜回忆又汹涌\n孤单的我没人陪我过冬\n孤单的痛没有人心疼\n爱那么真情那么浓\n那么伤害为何要相逢\n伤那么重心那么痛\n那么爱你也没让我赢\n孤单的人过着孤单的冬\n孤单的心想你发了疯\n孤单的我伴着孤单的灯\n孤单的梦最终一场空\n孤单的夜回忆又汹涌\n孤单的我没人陪我过冬\n孤单的痛没有人心疼',
 '/audio/lifafa_gudandong.mp3',NULL,NULL,NULL,'伤感民谣','/cover/cover04.jpg'),

-- 5 林俊杰 修炼爱情
('修炼爱情','易家扬','林俊杰',
 '凭什么要失望\n藏眼泪到心脏\n往事不会说谎别跟它为难\n我们两人之间不需要这样\n我想\n修炼爱情的心酸\n学会放好以前的渴望\n我们那些信仰\n要忘记多难\n远距离的欣赏\n近距离的迷惘\n谁说太阳会找到月亮\n别人有的爱\n我们不可能模仿\n修炼爱情的悲欢\n我们这些努力不简单\n快乐炼成泪水 是一种勇敢\n几年前的幻想 几年后的原谅\n为一张脸去养一身伤\n别讲想念我 我会受不了这样\n记忆它真嚣张\n路灯把痛点亮\n情人一起看过多少次月亮\n它在天空看过多少次遗忘 多少心慌\n修炼爱情的心酸\n学会放好以前的渴望\n我们那些信仰\n要忘记多难\n远距离的欣赏\n近距离的迷惘\n谁说太阳会找到月亮\n别人有的爱\n我们不可能模仿\n修炼爱情的悲欢\n我们这些努力不简单\n快乐炼成泪水 是一种勇敢\n几年前的幻想 几年后的原谅\n为一张脸去养一身伤\n别讲想念我 我会受不了这样',
 '/audio/linjj_xiulianaiqing.mp3',NULL,NULL,NULL,'华语抒情流行','/cover/cover05.jpg'),

-- 6 林俊杰 无尽的思念
('无尽的思念','宋建彰','林俊杰',
 '墙角那朵枯萎的玫瑰\n忘了为何要凋谢\n散在地上撕落的岁月\n又一天 又一年 没感觉\n伤痕在心田 滚烫地蔓延\n烟绕过发间 你敷衍着抱歉\n断了的琴弦 弹奏着从前\n一起走过的路线没有终点\n昏黄的光线 照射陈旧的水面\n映出那朵玫瑰思念的画面\n墙角那朵枯萎的玫瑰\n忘了为何要凋谢\n散在地上撕落的岁月\n又一天 又一年 没感觉\n风吹破欺骗 你无法兑现\n那年的夏天 你许下的誓言\n断了的琴弦 弹奏着从前\n一起走过的路线没有终点\n昏黄的光线 照射陈旧的水面\n映出那朵玫瑰思念的画面\n断了的琴弦 弹奏着从前\n一起走过的路线没有终点\n昏黄的光线 照射陈旧的水面\n映出那朵玫瑰思念的画面\n你走的那天 我决定不掉泪\n迎风撑着眼帘用力不眨眼',
 '/audio/linjj_wujinsinian.mp3',NULL,NULL,NULL,'华语抒情流行','/cover/cover06.jpg'),

-- 7 林俊杰 波间带
('波间带','林秋离','林俊杰',
 '是无形的存在\n转速太快 撕开未来\n空间扭曲摇摆\n讯号传开 把你找出来\n当几个慌慌 几个张张\n几个夜晚 几个让我又忙又乱\n几个荒荒 几个唐唐\n几个夜晚 所以我不看不想\nWOO WOO YEAH\nWOO\n电波隔空传来 反弹波间地带\n快快快 反应不过来\nWOO\n瞬间闪过现在 一分钟的未来\n开开开 虚拟的接触真的存在\nWOO\n是无形的存在\n转速太快 撕开未来\n空间扭曲摇摆\n讯号传开 把你找出来\n当几个慌慌 几个张张\n几个夜晚 几个让我又忙又乱\n几个荒荒 几个唐唐\n几个夜晚 所以我不看不想\nWOO WOO YEAH\nWOO\n电波隔空传来 反弹波间地带\n快快快 反应不过来\nWOO\n瞬间闪过现在 一分钟的未来\n开开开 虚拟的接触真的存在',
 '/audio/linjj_bojiandai.mp3',NULL,NULL,NULL,'华语抒情流行','/cover/cover07.jpg'),

-- 8 王子健 山间有歌
('山间有歌','王子健','王子健',
 '哦远去的人啊\n一程一程山呐\n你会听到歌声吗\n唱着\n初夏的栀子花\n别在谁的头发\n送你一直到天涯\n好像每个日夜不停歇\n思念成心结\n一点一点地强烈\n清澈时间里的河\n唱着听不懂的歌\n流淌着\n致我心上的谁谁渡百转千回\n致我心疼的谁谁还在被风吹\n一场雨又落下\n你啊你会在哪\n我找不到回答\n天阴阴云霞\n谁在不安涂鸦\n她明眸无瑕\n远远望啊\n哦远去的人啊\n一程一程山呐\n你还会唱着歌吗\n唱着\n初夏的栀子花\n别在谁的头发\n陪你一直到天涯\n月下青山披上薄纱\n我想念的人她在哪\n屋檐下灯火婆娑\n思念一杯装不下',
 '/audio/wzj_shanjianyouge.mp3',NULL,NULL,NULL,'治愈民谣','/cover/cover08.jpg'),

-- 9 陈奕迅 黑洞
('黑洞','袁两半','曲世聪',
 '寂寞在流动 某些真的假的梦\n渗在午夜里隐隐的痛\n各有各苦衷 无论猜中与未中\n各有各用意 各有各不同\n但愿是场梦 对你当初的心动\n我自有办法 抗拒认同\n然而全无用 投入所有已断送\n我再笑着痛 你也不懂\n好比火星跟水星相恋\n有过灿烂影踪\n但你转到某一个时空\n失去了互通\n今天尽管可始终相拥\n眼里却没沟通\n没法对抗倒数这时钟\n一种爱千种刺痛\n夜幕是无尽 暗中多少个黑洞\n看着似是爱 星空飘送\n曾如何情重 曾是真挚与自信\n却跌进深渊 逐分钟失重\n好比火星跟水星相恋\n有过灿烂影踪\n但你转到某一个时空\n失去了互通\n今天尽管可始终相拥\n眼里却没沟通\n没法对抗倒数这时钟\n一种爱千种刺痛',
 '/audio/chenyx_heidong.mp3',NULL,NULL,NULL,'粤语伤感流行','/cover/cover09.jpg'),

-- 10 陶喆 爱我还是他
('爱我还是他','陶喆/娃娃','陶喆',
 '黑暗中的我们都没有说话\n你只想回家 不想你回家\n寂寞深得像海太让人害怕\n温柔你的手 轻轻揉着我的发\n你的眉眼说\n你好渴望我拥抱\n你身体却在拼命逃 当欲望在燃烧\n你爱我还是他\n是不是真的他有比我好\n你为谁在挣扎\n你爱我还是他\n就说出你想说的真心话\n你到底要跟我还是他\n爱爱爱他\n这是不是命运对我的惩罚\n爱你也没办法\n恨你也没办法\n陷在这个漩涡只想挣脱它\n拉住你的手\n却让我也被拖下\n你的眉眼说\n你渴望我拥抱\n每当爱变成了煎熬\n你就开始要逃\n你爱我还是他\n是不是我可以做得更好\n让你不再挣扎\n你爱我还是他\n我宁愿听到残忍的回答\n也不要再被耍\n你爱我还是他\n我为你找了一百个理由\n我就是那么傻\n你爱我还是他\n是否沉默代替你的回答\n我应该明白吧\nYeah爱我还是他 喔\n你都已看不到我们的好\n我还为谁牵挂\n喔你爱我还是他\n是否沉默就是你的回答\n我们都别挣扎\n去爱他',
 '/audio/taozhe_aiwohaishita.mp3',NULL,NULL,NULL,'R&B情歌','/cover/cover10.jpg');

--
INSERT INTO playlist(user_id,name)
VALUES
    (1,'周杰伦精选集'),
    (1,'伤感情歌集'),
    (1,'治愈民谣歌单'),
    (1,'华语抒情流行歌单');

-- 歌单关联不变，song自增id1~10顺序执行直接用
INSERT INTO playlist_song(playlist_id,song_id)
VALUES
    (1,1),(1,2),(1,3),
    (2,4),(2,9),(2,10),
    (3,8),
    (4,5),(4,6),(4,7);