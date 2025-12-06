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
 * 学生管理控制器
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private UserService userService;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 获取服务实例信息（用于负载均衡验证）
     */
    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, String>> getInstanceInfo() {
        return ResponseEntity.ok(Map.of(
                "service", "user-service",
                "port", serverPort,
                "timestamp", LocalDateTime.now().toString()));
    }

    /**
     * 获取所有学生
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllStudents() {
        List<User> students = userService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    /**
     * 根据ID获取单个学生
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getStudentById(@PathVariable String id) {
        return userService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 按学号查询学生
     */
    @GetMapping("/studentId/{studentId}")
    public ResponseEntity<User> getStudentByStudentId(@PathVariable String studentId) {
        return userService.getStudentByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建学生
     */
    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody User user) {
        try {
            user.setCreatedAt(LocalDateTime.now());
            User createdStudent = userService.createStudent(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 更新学生
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @Valid @RequestBody UpdateUserDTO userDetails) {
        try {
            User updatedStudent = userService.updateStudent(id, userDetails);
            return ResponseEntity.ok(updatedStudent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            userService.deleteStudent(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
