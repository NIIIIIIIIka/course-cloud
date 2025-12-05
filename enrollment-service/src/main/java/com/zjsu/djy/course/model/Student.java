package com.zjsu.djy.course.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "student",
        uniqueConstraints = {@UniqueConstraint(columnNames = "id")})
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private String id;          // 系统生成UUID（唯一标识）

    private String studentId;   // 学号（全局唯一）

    private String name;        // 姓名（必填）


    private String major;       // 专业（必填）

    private Integer grade;      // 入学年份（必填）

    @Column(unique = true)
    private String email;       // 邮箱（必填，格式校验）

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 创建时间戳（系统生成）


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();

    }

    public void setId(String id) {
        this.id=id;
    }


}