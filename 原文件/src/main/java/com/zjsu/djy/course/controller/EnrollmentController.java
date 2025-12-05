package com.zjsu.djy.course.controller;

import com.zjsu.djy.course.common.ApiResponse;
import com.zjsu.djy.course.model.Enrollment;
import com.zjsu.djy.course.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选课管理API：极简实现，对应文档1-53至1-65的选课管理接口
 */
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    // 查询所有选课记录（GET /api/enrollments）
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEnrollments() {
        return success(enrollmentService.getAllEnrollments());
    }

    // 查询单个选课记录（GET /api/enrollments/{id}）
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEnrollmentById(@PathVariable String id) {
        return success(enrollmentService.getEnrollmentById(id));
    }

    // 按课程查询选课记录（GET /api/enrollments/course/{courseId}）
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> getByCourse(@PathVariable String courseId) {
        return success(enrollmentService.getEnrollmentsByCourseId(courseId));
    }

    // 按学生查询选课记录（GET /api/enrollments/student/{studentId}）
    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getByStudent(@PathVariable String studentId) {
        return success(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    // 学生选课（POST /api/enrollments）
    @PostMapping
    public ResponseEntity<Map<String, Object>> enroll(@RequestBody Enrollment enrollment) {
        return success(enrollmentService.enrollCourse(enrollment), HttpStatus.CREATED);
    }

    // 学生退课（DELETE /api/enrollments/{id}）
    @DeleteMapping("/{id}")
    public ApiResponse<Void> drop(@PathVariable String id) {
        enrollmentService.dropCourse(id);
        return ApiResponse.success("删除成功！");
    }

    // 复用成功响应工具方法
    private ResponseEntity<Map<String, Object>> success(Object data) {
        return success(data, HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> success(Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "Success");
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }
}