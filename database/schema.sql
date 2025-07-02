-- 游戏资源分享平台数据库表结构
-- 创建数据库
CREATE DATABASE IF NOT EXISTS yhh_game_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE yhh_game_platform;

-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `role` varchar(20) DEFAULT 'USER' COMMENT '角色：USER-普通用户，ADMIN-管理员',
  `vip_level` tinyint DEFAULT '0' COMMENT 'VIP等级',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 分类表
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `code` varchar(50) NOT NULL COMMENT '分类编码',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID，0表示顶级分类',
  `sort` int DEFAULT '0' COMMENT '排序',
  `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- 资源表
CREATE TABLE `resource` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `title` varchar(200) NOT NULL COMMENT '资源标题',
  `description` text COMMENT '资源描述',
  `content` longtext COMMENT '资源详细内容',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片URL',
  `images` text COMMENT '资源图片URL列表，JSON格式',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签，逗号分隔',
  `download_url` varchar(500) DEFAULT NULL COMMENT '下载地址',
  `download_password` varchar(100) DEFAULT NULL COMMENT '下载密码',
  `file_size` varchar(50) DEFAULT NULL COMMENT '文件大小',
  `version` varchar(50) DEFAULT NULL COMMENT '版本号',
  `platform` varchar(50) DEFAULT NULL COMMENT '平台：PC、Android、iOS等',
  `language` varchar(50) DEFAULT NULL COMMENT '语言：中文、英文等',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `is_recommend` tinyint DEFAULT '0' COMMENT '是否推荐：0-否，1-是',
  `is_hot` tinyint DEFAULT '0' COMMENT '是否热门：0-否，1-是',
  `view_count` bigint DEFAULT '0' COMMENT '浏览次数',
  `download_count` bigint DEFAULT '0' COMMENT '下载次数',
  `like_count` bigint DEFAULT '0' COMMENT '点赞次数',
  `comment_count` bigint DEFAULT '0' COMMENT '评论次数',
  `create_user_id` bigint NOT NULL COMMENT '创建用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_user_id` (`create_user_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_view_count` (`view_count`),
  FULLTEXT KEY `ft_title_description` (`title`,`description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源表';

-- 评论表
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父评论ID，0表示顶级评论',
  `content` text NOT NULL COMMENT '评论内容',
  `like_count` bigint DEFAULT '0' COMMENT '点赞次数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-删除，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 评论点赞表
CREATE TABLE `comment_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `comment_id` bigint NOT NULL COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comment_user` (`comment_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论点赞表';

-- 资源点赞表
CREATE TABLE `resource_like` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_resource_user` (`resource_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='资源点赞表';

-- 用户浏览记录表
CREATE TABLE `user_view_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `resource_id` bigint NOT NULL COMMENT '资源ID',
  `view_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_view_time` (`view_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户浏览记录表';

-- 系统配置表
CREATE TABLE `system_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(20) DEFAULT 'STRING' COMMENT '配置类型：STRING、NUMBER、BOOLEAN、JSON',
  `description` varchar(255) DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE `operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) NOT NULL COMMENT '操作类型',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `status` tinyint DEFAULT '1' COMMENT '操作状态：0-失败，1-成功',
  `error_msg` text COMMENT '错误信息',
  `execute_time` bigint DEFAULT NULL COMMENT '执行时间(毫秒)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation` (`operation`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 插入初始数据

-- 插入管理员用户
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$7JB720yubVSOfvVWdBYoOeymQxqZqHxqHxqHxqHxqHxqHxqHxqHxq', '管理员', 'admin@example.com', 'ADMIN', 1);

-- 插入分类数据
INSERT INTO `category` (`name`, `code`, `parent_id`, `sort`, `description`) VALUES
('游戏', 'games', 0, 1, '游戏分类'),
('手机游戏', 'mobilegames', 1, 1, '手机游戏'),
('电脑游戏', 'pcgames', 1, 2, '电脑游戏'),
('Steam游戏', 'steamgame', 1, 3, 'Steam游戏'),
('国区游戏', 'domesticgames', 4, 1, '国区游戏'),
('锁区游戏', 'region-locked-games', 4, 2, '锁区游戏'),
('折扣手游', 'mg', 0, 2, '折扣手游'),
('入站必看', 'announcement', 0, 0, '入站必看');

-- 插入系统配置
INSERT INTO `system_config` (`config_key`, `config_value`, `config_type`, `description`) VALUES
('site_name', '游戏资源分享平台', 'STRING', '网站名称'),
('site_description', '专注游戏资源分享，提供最新游戏下载', 'STRING', '网站描述'),
('site_keywords', '游戏,下载,资源,分享', 'STRING', '网站关键词'),
('upload_max_size', '104857600', 'NUMBER', '上传文件最大大小(字节)'),
('allowed_file_types', 'jpg,jpeg,png,gif,webp', 'STRING', '允许上传的文件类型'),
('comment_audit', 'false', 'BOOLEAN', '评论是否需要审核'); 