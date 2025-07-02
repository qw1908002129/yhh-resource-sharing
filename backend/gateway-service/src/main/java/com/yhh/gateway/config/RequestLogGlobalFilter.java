package com.yhh.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestLogGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        String clientIp = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown";

        log.info("外部请求: {} {}{} from IP: {}", method, path, query != null ? ("?" + query) : "", clientIp);

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange)
            .doOnSuccess(aVoid -> {
                ServerHttpResponse response = exchange.getResponse();
                log.info("响应: {} {} -> 状态码: {}，耗时: {}ms",
                        method, path, response.getStatusCode(), System.currentTimeMillis() - startTime);
            })
            .doOnError(throwable -> {
                log.error("请求异常: {} {} -> 错误: {}", method, path, throwable.getMessage(), throwable);
            });
    }

    @Override
    public int getOrder() {
        return -1; // 保证优先级高于其他过滤器
    }
} 