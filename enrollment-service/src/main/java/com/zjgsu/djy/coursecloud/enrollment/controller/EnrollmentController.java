package com.zjgsu.djy.coursecloud.enrollment.controller;

import com.zjgsu.djy.coursecloud.enrollment.dto.EnrollmentRequest;
import com.zjgsu.djy.coursecloud.enrollment.model.Enrollment;
import com.zjgsu.djy.coursecloud.enrollment.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
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
}

