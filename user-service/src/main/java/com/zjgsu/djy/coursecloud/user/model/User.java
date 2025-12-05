package com.zjgsu.djy.coursecloud.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Entity
@Table(name = "user", indexes = {
    @Index(name = "UK_user_userId", columnList = "user_id", unique = true),
    @Index(name = "UK_user_email", columnList = "email", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @NotBlank(message = "用户编号不能为空")
    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private String userId;

    @NotBlank(message = "姓名不能为空")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "用户类型不能为空")
    @Column(name = "user_type", nullable = false, length = 20)
    private String userType;

    @Column(name = "major", length = 255)
    private String major;

    @Column(name = "grade")
    private Integer grade;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}

