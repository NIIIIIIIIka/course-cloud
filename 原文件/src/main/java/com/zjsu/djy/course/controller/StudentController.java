package com.zjsu.djy.course.controller;

import com.zjsu.djy.course.common.ApiResponse;
import com.zjsu.djy.course.model.Student;
import com.zjsu.djy.course.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理API：极简实现，对应文档1-26至1-44的学生管理接口
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    // 查询所有学生（GET /api/students）
    @GetMapping
    public ApiResponse<Object>  getAllStudents() {

        return ApiResponse.success(studentService.getAllStudents());
    }

    // 查询单个学生（GET /api/students/{id}）
    @GetMapping("/{id}")
    public ApiResponse<Object>  getStudentById(@PathVariable String id) {
        return ApiResponse.success(studentService.getStudentById(id));
    }

    // 创建学生（POST /api/students）
    @PostMapping
    public ApiResponse<Object>  createStudent(@RequestBody Student student) {
        return ApiResponse.created(studentService.createStudent(student));
    }

    // 更新学生（PUT /api/students/{id}）
    @PutMapping("/{id}")
    public ApiResponse<Object>  updateStudent(
            @PathVariable String id,
            @RequestBody Student student
    ) {
        return ApiResponse.success(studentService.updateStudent(id, student));
    }

    // 删除学生（DELETE /api/students/{id}）
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ApiResponse.success("删除成功！");
    }

    // 复用成功响应工具方法（同CourseController）
}