package com.zjgsu.djy.coursecloud.enrollment.dto;

import lombok.Data;

@Data
public class StudentDto {
    private String id;
    private String studentName;
    private String userId; // 学号（可选）
}