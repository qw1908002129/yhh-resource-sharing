package com.yhh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhh.user.dto.UserLoginDTO;
import com.yhh.user.dto.UserRegisterDTO;
import com.yhh.user.entity.User;
import com.yhh.user.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO registerDTO);
    
    /**
     * 用户登录
     */
    String login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户名获取用户
     */
    User getByUsername(String username);
    
    /**
     * 根据ID获取用户VO
     */
    UserVO getUserVOById(Long id);
    
    /**
     * 更新用户信息
     */
    UserVO updateUserInfo(Long id, User user);
    
    /**
     * 修改密码
     */
    void changePassword(Long id, String oldPassword, String newPassword);
    
    /**
     * 删除用户（逻辑删除）
     */
    void deleteUser(Long id);
    
    /**
     * 批量删除用户（逻辑删除）
     */
    void batchDeleteUsers(Long[] ids);
    
    /**
     * 永久删除用户（物理删除）
     */
    void permanentlyDeleteUser(Long id);

    // TODO: 这是一个临时方法，用于生成密码哈希，测试完成后请删除
    String encodePassword(String rawPassword);
} 