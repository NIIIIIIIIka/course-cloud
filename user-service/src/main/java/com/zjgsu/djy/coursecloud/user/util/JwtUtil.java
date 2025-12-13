package com.zjgsu.djy.coursecloud.user.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 从配置文件读取 JWT 密钥（需和网关配置一致）
    @Value("${jwt.secret}")
    private String secret;

    // Token 有效期（毫秒）
    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    /**
     * 生成 JWT Token
     * @param userId 用户ID
     * @param name 用户名/学号
     * @param role 用户角色（ADMIN/STUDENT）
     * @return Token 字符串
     */
    public String generateToken(String userId, String name, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // 使用 HS512 算法签名
        Key signingKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(userId)             // Token 主题：用户ID
                .claim("name", name) // 自定义声明：用户名/学号
                .claim("role", role)         // 自定义声明：用户角色
                .setIssuedAt(now)               // 签发时间
                .setExpiration(expiryDate)      // 过期时间
                .signWith(signingKey, SignatureAlgorithm.HS512) // 签名
                .compact();
    }

    /**
     * 解析 Token 获取用户信息
     */
    public Claims parseToken(String token) {
        Key signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 Token 有效性
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return false;
        }
    }
}