package com.yhh.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerLogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Pointcut("execution(* com.yhh.user.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logRequest(JoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();
        String params = objectMapper.writeValueAsString(args);
        log.info("请求方法: {}", methodName);
        log.info("请求参数: {}", params);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) throws Throwable {
        String response = objectMapper.writeValueAsString(result);
        log.info("响应结果: {}", response);
    }
} 