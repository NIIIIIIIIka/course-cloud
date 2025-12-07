package com.zjgsu.djy.coursecloud.enrollment.dto;

import lombok.Data;

/**
 * 课程数据传输对象（适配接口返回/Feign调用）
 */
@Data
public class CourseDto {
    // 与你EnrollmentRequest的ID字段类型保持一致（String）
    private String courseId;
    // 课程名称
    private String courseName;
    // 可选：课程编号/其他扩展字段
    private String courseCode;
    // 可选：课程学分、授课老师等业务字段（根据实际需求添加）
}