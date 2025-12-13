package com.zjgsu.djy.coursecloud.user.controller;

import com.zjgsu.djy.coursecloud.user.model.UpdateUserDTO;
import com.zjgsu.djy.coursecloud.user.model.User;
import com.zjgsu.djy.coursecloud.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器（适配网关认证）
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private static final String USER_TYPE_STUDENT = "student";
    private static final String USER_TYPE_TEACHER = "teacher";
    private static final String USER_TYPE_ADMIN = "admin";
    @Autowired
    private UserService userService;

    @Value("${server.port}")
    private String serverPort;

    // ========== 1. 保留原有接口，补充认证/权限逻辑 ==========

    /**
     * 获取服务实例信息（用于负载均衡验证）
     */
    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, String>> getInstanceInfo(
            // 从网关透传的请求头获取当前登录用户信息
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Name", required = false) String username,
            @RequestHeader(value = "X-User-Role", required = false) String userRole) {

        // 打印用户信息（验证透传成功）
        System.out.println("当前登录用户：ID=" + userId + ", 用户名=" + username + ", 角色=" + userRole);

        return ResponseEntity.ok(Map.of(
                "service", "user-service",
                "port", serverPort,
                "timestamp", LocalDateTime.now().toString(),
                "currentUser", username != null ? username : "未认证用户"));
    }

    /**
     * 获取所有学生（仅管理员/学生可访问，学生仅能看自己）
     */
    @GetMapping
    public ResponseEntity<?> getAllStudents(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String userRole) {

        // 权限控制：ADMIN 可查看所有学生，STUDENT 仅能查看自己
        if (USER_TYPE_ADMIN.equals(userRole)) {
            List<User> students = userService.getAllStudents();
            return ResponseEntity.ok(students);
        } else if (USER_TYPE_STUDENT.equals(userRole)) {
            return userService.getStudentById(userId)
                    .map(student -> ResponseEntity.ok(List.of(student))) // 返回自己的信息
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "无权限访问"));
        }
    }

    /**
     * 根据ID获取单个学生（管理员可查所有，学生仅可查自己）
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String userRole) {

        // 权限校验
        if (!USER_TYPE_ADMIN.equals(userRole) && !userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "仅可查看自己的信息"));
        }

        return userService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 按学号查询学生（权限控制同上）
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<?> getStudentByStudentId(
            @PathVariable String studentId,
            @RequestHeader("X-User-Role") String userRole,
            @RequestHeader("X-User-Id") String userId) {

        // 先查询学生ID，再校验权限
        return userService.getStudentByStudentId(studentId)
                .map(student -> {
                    if (USER_TYPE_ADMIN.equals(userRole) || student.getId().equals(userId)) {
                        return ResponseEntity.ok(student);
                    } else {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("error", "仅可查看自己的信息"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建学生（仅管理员可操作）
     */
    @PostMapping
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody User user,
            @RequestHeader("X-User-Role") String userRole) {

        // 权限校验：仅ADMIN可创建学生
        if (!USER_TYPE_ADMIN.equals(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "仅管理员可创建学生"));
        }

        try {
            user.setCreatedAt(LocalDateTime.now());
            user.setUserType(USER_TYPE_STUDENT); // 强制设置为学生角色
            User createdStudent = userService.createStudent(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 更新学生（管理员可更新所有，学生仅可更新自己）
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserDTO userDetails,
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String userRole) {

        // 权限校验
        if (!USER_TYPE_ADMIN.equals(userRole) && !userId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "仅可更新自己的信息"));
        }

        try {
            User updatedStudent = userService.updateStudent(id, userDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 删除学生（仅管理员可操作）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(
            @PathVariable String id,
            @RequestHeader("X-User-Role") String userRole) {

        // 权限校验：仅ADMIN可删除学生
        if (!USER_TYPE_ADMIN.equals(userRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "仅管理员可删除学生"));
        }

        try {
            userService.deleteStudent(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}