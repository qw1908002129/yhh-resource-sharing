package com.yhh.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN", "管理员"),
    USER("USER", "普通用户");
    
    private final String code;
    private final String desc;
    
    UserRole(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
} 