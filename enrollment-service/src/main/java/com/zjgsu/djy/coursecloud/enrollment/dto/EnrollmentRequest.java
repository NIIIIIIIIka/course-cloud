package com.zjgsu.djy.coursecloud.enrollment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EnrollmentRequest {
    
    @NotBlank(message = "课程ID不能为空")
    private String courseId;
    
    @NotBlank(message = "学生ID不能为空")
    private String studentId;
}

