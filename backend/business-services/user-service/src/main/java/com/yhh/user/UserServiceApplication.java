package com.yhh.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户服务启动类
 */
@SpringBootApplication(exclude = {
    RedisAutoConfiguration.class,
    RedisRepositoriesAutoConfiguration.class
})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.yhh.user.mapper")
@ComponentScan(basePackages = {"com.yhh.user", "com.yhh.common"})
public class UserServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
} 