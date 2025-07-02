package com.yhh.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    
    @Test
    public void generatePasswordHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123456";
        String encodedPassword = encoder.encode(password);
        System.out.println("Generated hash for password '" + password + "': " + encodedPassword);
        
        // 验证密码
        boolean matches = encoder.matches(password, encodedPassword);
        System.out.println("Password verification test: " + matches);
    }
} 