package com.zjgsu.djy.coursecloud.filter;

import com.zjgsu.djy.coursecloud.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    // 白名单：无需认证的路径（登录、注册接口）
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
            "/api/courses/instance-info",
            "/api/students/instance-info"

    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        System.out.println("JWT Filter - 检查路径: " + path + ", 方法: " + method);
        // 1. 白名单路径直接放行
        if (WHITE_LIST.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // 2. 从请求头获取 Authorization（格式：Bearer <token>）
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // 无 Token 或格式错误，返回 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 3. 提取 Token（去掉 "Bearer " 前缀）
        String token = authorizationHeader.substring(7);

        // 4. 验证 Token 有效性
        if (!jwtUtil.validateToken(token)) {
            // 1. 标记 401
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            // 2. 告诉前端这是 JSON
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            // 3. 组装提示
            String body = "{\"error\":\"Token 无效或已过期\",\"code\":401}";
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            // 4. 写出去并结束
            return exchange.getResponse().writeWith(
                            Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)))
                    .then(exchange.getResponse().setComplete());
        }

        // 5. 解析 Token，获取用户信息
        Claims claims = jwtUtil.parseToken(token);
        String userId = claims.getSubject();
        String username = claims.get("name", String.class);
        String role = claims.get("role", String.class);

        // 6. 将用户信息添加到请求头，转发给后端服务
        exchange = exchange.mutate()
                .request(request -> request
                        .header("X-User-Id", userId)
                        .header("X-User-Name", username)
                        .header("X-User-Role", role)
                )
                .build();

        // 7. 转发请求到下游服务
        return chain.filter(exchange);
    }

    /**
     * 过滤器执行顺序：值越小，优先级越高（-100 确保在路由过滤器前执行）
     */
    @Override
    public int getOrder() {
        return -100;
    }
}