package com.example.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.handler.predicate.ReadBodyPredicateFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import sun.rmi.runtime.Log;

import java.util.Map;

/**
 * @Description:
 * @author: LinQin
 * @date: 2020/01/08
 */
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("前执行");
        // String token = exchange.getRequest().getQueryParams().getFirst("authToken");
        String token = exchange.getRequest().getHeaders().getFirst("authToken");
        if (token == null || token.isEmpty()) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Mono.just(response.bufferFactory().wrap("无权限".getBytes())));
        }

        Map<String, Object> data = exchange.getAttribute("cachedRequestBodyObject");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("后执行")));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
