package com.yhh.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.common.result.ResultCode;
import com.yhh.user.dto.UserLoginDTO;
import com.yhh.user.dto.UserRegisterDTO;
import com.yhh.user.entity.User;
import com.yhh.user.mapper.UserMapper;
import com.yhh.user.service.UserService;
import com.yhh.user.utils.JwtUtils;
import com.yhh.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Override
    public UserVO register(UserRegisterDTO registerDTO) {
        log.info("开始用户注册，用户名: {}", registerDTO.getUsername());
        
        // 验证密码
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            log.error("用户注册失败，两次密码不一致，用户名: {}", registerDTO.getUsername());
            throw new RuntimeException("两次密码不一致");
        }
        
        // 检查用户名是否存在
        if (getByUsername(registerDTO.getUsername()) != null) {
            log.error("用户注册失败，用户名已存在: {}", registerDTO.getUsername());
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (StringUtils.hasText(registerDTO.getEmail())) {
            log.debug("检查邮箱是否存在: {}", registerDTO.getEmail());
            if (baseMapper.countByEmail(registerDTO.getEmail()) > 0) {
                log.error("用户注册失败，邮箱已存在: {}", registerDTO.getEmail());
                throw new RuntimeException("邮箱已存在");
            }
        }
        
        // 检查手机号是否存在
        if (StringUtils.hasText(registerDTO.getPhone())) {
            log.debug("检查手机号是否存在: {}", registerDTO.getPhone());
            if (baseMapper.countByPhone(registerDTO.getPhone()) > 0) {
                log.error("用户注册失败，手机号已存在: {}", registerDTO.getPhone());
                throw new RuntimeException("手机号已存在");
            }
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1);
        user.setRole(registerDTO.getRole().getCode());
        user.setVipLevel(0);
        user.setBalance(new java.math.BigDecimal("0.00"));
        
        log.debug("准备保存用户到数据库: {}", user.getUsername());
        save(user);
        log.info("用户注册成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
        
        // 直接从保存的用户实体创建VO对象，而不是重新查询
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }
    
    @Override
    public String login(UserLoginDTO loginDTO) {
        log.info("用户登录尝试，用户名: {}", loginDTO.getUsername());
        
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            log.error("用户登录失败，用户不存在: {}", loginDTO.getUsername());
            throw new RuntimeException("用户不存在");
        }
        
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            log.error("用户登录失败，密码错误，用户名: {}", loginDTO.getUsername());
            throw new RuntimeException("密码错误");
        }
        
        if (user.getStatus() == 0) {
            log.error("用户登录失败，用户已被禁用，用户名: {}", loginDTO.getUsername());
            throw new RuntimeException("用户已被禁用");
        }
        
        // 生成JWT token
        String token = generateToken(user);
        log.info("用户登录成功，用户ID: {}, 用户名: {}", user.getId(), user.getUsername());
        
        return token;
    }
    
    @Override
    public User getByUsername(String username) {
        log.debug("根据用户名查询用户: {}", username);
        User user = baseMapper.selectByUsername(username);
        if (user != null) {
            log.debug("找到用户: {}, ID: {}", username, user.getId());
        } else {
            log.debug("未找到用户: {}", username);
        }
        return user;
    }
    
    @Override
    public UserVO getUserVOById(Long id) {
        log.debug("根据ID查询用户信息: {}", id);
        User user = getById(id);
        if (user == null) {
            log.warn("用户不存在，ID: {}", id);
            return null;
        }
        
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }
    
    @Override
    public UserVO updateUserInfo(Long id, User user) {
        log.info("更新用户信息，用户ID: {}", id);
        
        User existUser = getById(id);
        if (existUser == null) {
            log.error("更新用户信息失败，用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在");
        }
        
        // 只允许更新部分字段
        existUser.setNickname(user.getNickname());
        existUser.setEmail(user.getEmail());
        existUser.setPhone(user.getPhone());
        existUser.setAvatar(user.getAvatar());
        
        updateById(existUser);
        log.info("用户信息更新成功，用户ID: {}", id);
        
        return getUserVOById(id);
    }
    
    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("用户修改密码，用户ID: {}", id);
        
        User user = getById(id);
        if (user == null) {
            log.error("修改密码失败，用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在");
        }
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("修改密码失败，原密码错误，用户ID: {}", id);
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
        log.info("用户密码修改成功，用户ID: {}", id);
    }
    
    /**
     * 生成JWT token
     */
    private String generateToken(User user) {
        log.debug("生成JWT token，用户ID: {}", user.getId());
        boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());
        return jwtUtils.generateToken(user.getId(), user.getUsername(), isAdmin);
    }
    
    // TODO: 这是一个临时方法，用于生成密码哈希，测试完成后请删除
    @Override
    public String encodePassword(String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        log.info("原始密码: {}, 加密后: {}", rawPassword, encoded);
        
        // 验证密码
        boolean matches = passwordEncoder.matches(rawPassword, encoded);
        log.info("密码验证测试: {}", matches);
        
        return encoded;
    }
    
    @Override
    public void deleteUser(Long id) {
        log.info("开始删除用户，用户ID: {}", id);
        
        User user = getById(id);
        if (user == null) {
            log.error("删除用户失败，用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在");
        }
        
        // 检查是否为管理员用户，防止误删
        if ("ADMIN".equals(user.getRole())) {
            log.error("删除用户失败，不能删除管理员用户，用户ID: {}", id);
            throw new RuntimeException("不能删除管理员用户");
        }
        
        // 逻辑删除
        boolean result = removeById(id);
        if (result) {
            log.info("用户删除成功，用户ID: {}, 用户名: {}", id, user.getUsername());
        } else {
            log.error("用户删除失败，用户ID: {}", id);
            throw new RuntimeException("删除用户失败");
        }
    }
    
    @Override
    public void batchDeleteUsers(Long[] ids) {
        log.info("开始批量删除用户，用户ID数量: {}", ids.length);
        
        if (ids == null || ids.length == 0) {
            log.warn("批量删除用户失败，用户ID列表为空");
            throw new RuntimeException("用户ID列表不能为空");
        }
        
        // 检查是否存在管理员用户
        for (Long id : ids) {
            User user = getById(id);
            if (user != null && "ADMIN".equals(user.getRole())) {
                log.error("批量删除用户失败，不能删除管理员用户，用户ID: {}", id);
                throw new RuntimeException("不能删除管理员用户，用户ID: " + id);
            }
        }
        
        // 批量逻辑删除
        boolean result = removeByIds(java.util.Arrays.asList(ids));
        if (result) {
            log.info("批量删除用户成功，删除数量: {}", ids.length);
        } else {
            log.error("批量删除用户失败");
            throw new RuntimeException("批量删除用户失败");
        }
    }
    
    @Override
    public void permanentlyDeleteUser(Long id) {
        log.warn("开始永久删除用户，用户ID: {}", id);
        
        User user = getById(id);
        if (user == null) {
            log.error("永久删除用户失败，用户不存在，ID: {}", id);
            throw new RuntimeException("用户不存在");
        }
        
        // 检查是否为管理员用户
        if ("ADMIN".equals(user.getRole())) {
            log.error("永久删除用户失败，不能删除管理员用户，用户ID: {}", id);
            throw new RuntimeException("不能删除管理员用户");
        }
        
        // 物理删除（使用baseMapper直接删除）
        int result = baseMapper.deleteById(id);
        if (result > 0) {
            log.warn("用户永久删除成功，用户ID: {}, 用户名: {}", id, user.getUsername());
        } else {
            log.error("用户永久删除失败，用户ID: {}", id);
            throw new RuntimeException("永久删除用户失败");
        }
    }
} 