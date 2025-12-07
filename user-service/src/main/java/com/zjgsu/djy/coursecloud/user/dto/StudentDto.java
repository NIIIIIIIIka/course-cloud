package com.zjgsu.djy.coursecloud.user.dto;

import lombok.Data;

/**
 * 学生DTO（Feign调用专用）
 */
@Data
public class StudentDto {
    private String Id; // 与 User 实体的 studentId 字段对应
    private String studentName; // 与 User 实体的 name 字段对应
    private String userId; // 与 User 实体的 studentNo 字段对应（按需添加）
}