package com.yhh.user.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户VO
 */
@Data
public class UserVO {
    
    private Long id;
    
    private String username;
    
    private String nickname;
    
    private String email;
    
    private String avatar;
    
    private String phone;
    
    private Integer status;
    
    private String role;
    
    private Integer vipLevel;
    
    private BigDecimal balance;
    
    private LocalDateTime createTime;
} 