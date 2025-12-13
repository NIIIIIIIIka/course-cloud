package com.zjgsu.djy.coursecloud.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成 JWT Token
     * @param userId 用户 ID
     * @param name 用户名
     * @param role 用户角色
     * @return Token 字符串
     */
    public String generateToken(String userId, String name, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        // 构建 JWT Token（HS512 算法）
        return Jwts.builder()
                .setSubject(userId) // Token 主题（存储用户 ID）
                .claim("name", name) // 自定义声明：用户名
                .claim("role", role) // 自定义声明：用户角色
                .setIssuedAt(now) // 签发时间
                .setExpiration(expiryDate) // 过期时间
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // 签名算法与密钥
                .compact();
    }

    /**
     * 解析 Token，获取声明（Claims）
     * @param token JWT Token
     * @return Claims 包含用户信息
     * @throws ExpiredJwtException Token 过期
     * @throws UnsupportedJwtException 不支持的 Token 格式
     * @throws MalformedJwtException Token 格式错误
     * @throws SignatureException 签名验证失败
     * @throws IllegalArgumentException Token 为空
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 验证密钥
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证 Token 有效性
     * @param token JWT Token
     * @return true：有效；false：无效
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token); // 解析成功则 Token 有效
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取签名密钥（将配置的 secret 转换为 Key 对象）
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}