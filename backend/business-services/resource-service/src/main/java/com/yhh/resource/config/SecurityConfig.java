package com.yhh.resource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                // 1. 完全公开的接口
                .requestMatchers(
                    "/resource/list",           // 资源列表
                    "/resource/category/list",  // 分类列表
                    "/resource/*/download",     // 资源下载
                    "/resource/recommend",      // 推荐资源
                    "/resource/hot",           // 热门资源
                    "/uploads/**"              // 静态资源
                ).permitAll()
                // 2. 需要认证的接口
                .requestMatchers(
                    "/resource/upload/cover"    // 上传封面
                ).authenticated()
                // 3. 其他所有请求都需要认证
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
} 