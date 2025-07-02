package com.yhh.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhh.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);
    
    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM user WHERE email = #{email} AND deleted = 0")
    User selectByEmail(@Param("email") String email);
    
    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM user WHERE phone = #{phone} AND deleted = 0")
    User selectByPhone(@Param("phone") String phone);
    
    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND deleted = 0")
    int countByUsername(@Param("username") String username);
    
    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE email = #{email} AND deleted = 0")
    int countByEmail(@Param("email") String email);
    
    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(*) FROM user WHERE phone = #{phone} AND deleted = 0")
    int countByPhone(@Param("phone") String phone);
} 