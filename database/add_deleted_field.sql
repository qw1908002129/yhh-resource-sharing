-- 为user表添加逻辑删除字段
USE yhh_game_platform;

-- 添加deleted字段
ALTER TABLE `user` ADD COLUMN `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除' AFTER `balance`;

-- 为deleted字段添加索引
ALTER TABLE `user` ADD INDEX `idx_deleted` (`deleted`);

-- 更新现有数据的deleted字段为0（未删除）
UPDATE `user` SET `deleted` = 0 WHERE `deleted` IS NULL;

-- 验证字段添加成功
DESCRIBE `user`; 