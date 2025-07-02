package com.yhh.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhh.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String nickname;
    
    private String email;
    
    private String avatar;
    
    private String phone;
    
    private Integer status;
    
    private String role;
    
    private Integer vipLevel;
    
    private BigDecimal balance;
    
    /**
     * 逻辑删除字段：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;
} 