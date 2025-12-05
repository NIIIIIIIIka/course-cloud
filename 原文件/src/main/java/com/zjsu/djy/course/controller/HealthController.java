package com.zjsu.djy.course.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统健康检查控制器：提供数据库连通性验证接口
 */
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    // 注入JdbcTemplate，直接操作数据库（轻量验证，无需通过Repository）
    private final JdbcTemplate jdbcTemplate;

    /**
     * 数据库连通性验证接口
     * 调用方式：curl localhost:8080/health/db
     */
    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> checkDbHealth() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 执行轻量查询（查询MySQL版本，验证连接可用）
            String mysqlVersion = jdbcTemplate.queryForObject(
                    "SELECT VERSION()", String.class
            );
            // 执行表存在性验证（确保核心表course存在）
            Integer courseTableCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'course'",
                    Integer.class
            );

            // 组装成功响应
            result.put("status", "UP");
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("db", "MySQL");
            result.put("dbVersion", mysqlVersion);
            result.put("coreTableStatus", courseTableCount != null && courseTableCount > 0 ? "EXISTS" : "NOT_FOUND");
            result.put("message", "Database connection is healthy");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            // 组装失败响应（包含错误信息）
            result.put("status", "DOWN");
            result.put("timestamp", LocalDateTime.now().toString());
            result.put("db", "MySQL");
            result.put("message", "Database connection failed: " + e.getMessage().substring(0, 200)); // 截取部分错误信息，避免过长

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
        }
    }
}