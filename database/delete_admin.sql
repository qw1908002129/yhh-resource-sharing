-- 删除所有管理员账户
DELETE FROM `user` WHERE `role` = 'ADMIN';
 
-- 删除特定用户名的账户
DELETE FROM `user` WHERE `username` = 'admin'; 