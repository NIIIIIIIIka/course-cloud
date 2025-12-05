package com.zjsu.djy.course.controller;

import com.zjsu.djy.course.common.ApiResponse;
import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程管理API：极简实现，仅保留核心逻辑
 */
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    // 查询所有课程（GET /api/courses）
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return success(courses);
    }

    // 查询单个课程（GET /api/courses/{id}）
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable String id) {
        return success(courseService.getCourseById(id));
    }

    // 创建课程（POST /api/courses）
    @PostMapping
    public ApiResponse<Object>  createCourse(@RequestBody Course course) {
        if (course.getCode()==null)
            return ApiResponse.success("code is need");
//        return ApiResponse.success(course);
        Course saved = courseService.createCourse(course);
        return ApiResponse.created(saved); // 创建成功用201状态码
    }

    // 更新课程（PUT /api/courses/{id}）
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(
            @PathVariable String id,
            @RequestBody Course course
    ) {
        return success(courseService.updateCourse(id, course));
    }

    // 删除课程（DELETE /api/courses/{id}）
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ApiResponse.success("删除成功");
    }

    // 内部工具：构建成功响应（默认200状态码）
    private ResponseEntity<Map<String, Object>> success(Object data) {
        return success(data, HttpStatus.OK);
    }

    // 内部工具：构建带自定义状态码的成功响应
    private ResponseEntity<Map<String, Object>> success(Object data, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 201);          // 文档1-82规定的成功code
        response.put("message", "Success"); // 文档1-82规定的成功message
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }
}