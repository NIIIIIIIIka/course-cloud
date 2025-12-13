package com.zjgsu.djy.coursecloud.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;


/**
 * 用户实体类（适配JWT认证+权限控制）
 */
@Entity
@Table(name = "user", indexes = {
        @Index(name = "UK_user_userId", columnList = "user_id", unique = true),
        @Index(name = "UK_user_email", columnList = "email", unique = true),
        @Index(name = "UK_user_username", columnList = "username", unique = true) // 新增用户名唯一索引
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 主键（UUID自动生成）
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", length = 36, updatable = false)
    private String id;

    /**
     * 用户编号（学号/工号）
     */
    @NotBlank(message = "用户编号不能为空")
    @Column(name = "user_id", nullable = false, unique = true, length = 255)
    private String userId;

    /**
     * 登录密码（生产环境需加密存储）
     */
    @NotBlank(message = "密码不能为空")
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /**
     * 原user_type字段（保留兼容，可映射到role）
     * 备注：建议后续统一使用role，逐步废弃user_type
     */
    @Column(name = "user_type", length = 20)
    private String userType;

    /**
     * 专业
     */
    @Column(name = "major", length = 255)
    private String major;

    /**
     * 年级
     */
    @Column(name = "grade")
    private Integer grade;

    /**
     * 创建时间
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 自动填充创建时间（插入前）
     */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        // 自动映射user_type和role（兼容原有逻辑）
    }
}