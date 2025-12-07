package com.zjgsu.djy.coursecloud.user.controller;

import com.zjgsu.djy.coursecloud.user.dto.StudentDto;
import com.zjgsu.djy.coursecloud.user.model.User;
import com.zjgsu.djy.coursecloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Feign 调用适配控制器（专门给 enrollment-service 调用，不影响原有接口）
 */
@RestController
@RequestMapping("/api/users/students") // 与 EnrollmentService 的 UserClient 路径完全对齐
public class FeignStudentController {

    @Autowired
    private UserService userService; // 复用原有 Service，逻辑一致

    /**
     * Feign 调用专用：根据学生ID查询，返回 StudentDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentForFeign(@PathVariable String id) {
        // 复用原有 Service 逻辑（getStudentById），保证数据一致性
        Optional<User> userOptional = userService.getStudentById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 转换 User 实体 → StudentDto（核心：只返回需要的字段，屏蔽敏感信息）
        User user = userOptional.get();
        StudentDto dto = new StudentDto();
        dto.setId(user.getId());
        dto.setStudentName(user.getName());
        dto.setUserId(user.getUserId()); // 按需添加字段

        return ResponseEntity.ok(dto);
    }
}