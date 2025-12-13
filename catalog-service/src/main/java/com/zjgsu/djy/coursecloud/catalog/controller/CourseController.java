package com.zjgsu.djy.coursecloud.catalog.controller;

import com.zjgsu.djy.coursecloud.catalog.dto.CourseRequest;
import com.zjgsu.djy.coursecloud.catalog.model.Course;
import com.zjgsu.djy.coursecloud.catalog.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * 获取服务实例信息（用于负载均衡验证）
     */
    @GetMapping("/instance-info")
    public ResponseEntity<Map<String, String>> getInstanceInfo(    ) {
        return ResponseEntity.ok(Map.of(
                "service", "catalog-service",
                "port", serverPort,
                "timestamp", LocalDateTime.now().toString()));
    }

    /**
     * 获取所有课程
     * GET /api/courses
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(
            @RequestHeader(value = "X-User-Id", required = false) String userId
    ) {
        log.info("用户 {}（ID: {}）发起选课请求",  userId);
        List<Course> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    /**
     * 根据ID获取单个课程
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable String id) {
        return courseService.getCourseById(id)
                .map(course -> ResponseEntity.ok(course))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 根据课程编码查询课程
     * GET /api/courses/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String code) {
        return courseService.getCourseByCode(code)
                .map(course -> ResponseEntity.ok(course))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建课程
     * POST /api/courses
     */
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        try {
            Course course = courseService.createCourse(courseRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * 更新课程
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable String id,
            @Valid @RequestBody CourseRequest courseRequest) {
        try {
            Course course = courseService.updateCourse(id, courseRequest);
            return ResponseEntity.ok(course);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("不存在")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * 删除课程
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok(Map.of("message", "删除成功"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 错误响应类
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
