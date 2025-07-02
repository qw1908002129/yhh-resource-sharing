-- 删除已存在的管理员账户（如果有）
DELETE FROM `user` WHERE `username` = 'admin';

-- 插入新的管理员账户，密码是admin123456
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$snnOKPvQz7ZdYlCIhTxfTu8I6K2.ixWFg4OY.ZkrCQUS9sN8lJf7u', '管理员', 'admin@example.com', 'ADMIN', 1); 