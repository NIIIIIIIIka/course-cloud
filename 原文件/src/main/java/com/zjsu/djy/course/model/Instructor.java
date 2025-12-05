package com.zjsu.djy.course.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 嵌入式讲师信息（无独立表，嵌入course表）
 */
@Data
@Embeddable // 标记为可嵌入式对象

public class Instructor{
    @JsonProperty("id")
    @Column(name = "instructor_id", nullable = false)
    private String teacherId;

    @NotBlank(message = "讲师姓名不能为空")
    @Column(name = "instructor_name", nullable = false)
    private String name; // 讲师姓名

    @NotBlank(message = "讲师邮箱不能为空")
    @Email(message = "讲师邮箱格式不正确")
    @Column(name = "instructor_email", nullable = false)
    private String email; // 讲师邮箱

}