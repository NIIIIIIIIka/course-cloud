package com.zjgsu.djy.coursecloud.catalog.dto;

import lombok.Data;

/**
 * 课程DTO（Feign跨服务调用专用，仅暴露必要字段）
 */
@Data
public class CourseDto {
    private String courseId; // 对应Course实体的id/课程ID
    private String courseName; // 对应Course实体的name
    private String courseCode; // 对应Course实体的code
    // 可按需添加其他字段（如学分、授课老师等），但仅保留必要字段
}