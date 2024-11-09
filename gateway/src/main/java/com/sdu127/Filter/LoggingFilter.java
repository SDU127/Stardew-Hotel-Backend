package com.sdu127.Filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class LoggingFilter implements GlobalFilter {

    private static final Logger logger = LogManager.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求和响应对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取请求路径和请求来源地址
        String path = request.getURI().getPath();
        String remoteAddress = request.getRemoteAddress() != null ? request.getRemoteAddress().toString() : "unknown";

        // 打印请求信息
        logger.info("Request at {}: {} from {}", LocalDateTime.now(), path, remoteAddress);

        // 记录请求处理开始时间
        long startTime = System.currentTimeMillis();

        // 继续处理请求
        return chain.filter(exchange)
                .doOnTerminate(() -> {
                    // 计算请求处理的时长
                    long duration = System.currentTimeMillis() - startTime;
                    // 打印响应状态码和处理时长
                    logger.info("Request completed at {} with status {}, cost {} ms", path, response.getStatusCode(), duration);
                });
    }
}

