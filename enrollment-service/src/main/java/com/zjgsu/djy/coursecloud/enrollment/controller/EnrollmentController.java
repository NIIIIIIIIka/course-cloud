package com.zjgsu.djy.coursecloud.enrollment.controller;

import com.zjgsu.djy.coursecloud.enrollment.dto.EnrollmentRequest;
import com.zjgsu.djy.coursecloud.enrollment.model.Enrollment;
import com.zjgsu.djy.coursecloud.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final RestTemplate restTemplate;

    /**
     * 获取所有选课记录
     */
    @GetMapping
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

    /**
     * 按课程查询选课
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourseId(@PathVariable String courseId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * 按学生查询选课
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudentId(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }

    /**
     * 学生选课
     */
    @PostMapping
    public ResponseEntity<Enrollment> enrollCourse(@Valid @RequestBody EnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.enrollCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
    }

    /**
     * 学生退课
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> dropCourse(@PathVariable String id) {
        enrollmentService.dropCourse(id);
        return ResponseEntity.ok(Map.of("message", "退课成功"));

    }
    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, String>> getInstanceInfo() {

        // 打印用户信息（验证透传成功）
//        System.out.println("当前登录用户：ID=" + userId + ", 用户名=" + username + ", 角色=" + userRole);

        return ResponseEntity.ok(Map.of("message", "实例测试成功"));
    }

    /**
     * 测试User服务负载均衡
     */
//    @GetMapping("/test/user-instance")
//    public ResponseEntity<Map<String, Object>> testUserServiceLoadBalance() {
//        Map<String, Object> response = restTemplate.getForObject("http://user-service/api/students/instance-info",
//                Map.class);
//        return ResponseEntity.ok(response);
//    }

    /**
     * 测试Catalog服务负载均衡
     */
//    @GetMapping("/test/catalog-instance")
//    public ResponseEntity<Map<String, Object>> testCatalogServiceLoadBalance() {
//        Map<String, Object> response = restTemplate.getForObject("http://catalog-service/api/courses/instance-info",
//                Map.class);
//        return ResponseEntity.ok(response);
//    }
}
