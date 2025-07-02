package com.yhh.resource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 资源服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.yhh.resource.mapper")
@ComponentScan(basePackages = {"com.yhh.resource", "com.yhh.common"})
public class ResourceServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ResourceServiceApplication.class, args);
    }
} 