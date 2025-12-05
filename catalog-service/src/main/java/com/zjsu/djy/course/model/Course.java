package com.zjsu.djy.course.model;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 课程实体
 */
@Data
@Entity
@Table(name = "course",
        uniqueConstraints = {@UniqueConstraint(columnNames = "code")})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;                // 课程ID（系统生成UUID）

    @NotBlank(message = "课程编码不能为空")
    @Column(unique = true)
    private String code;              // 课程编码（如CS101）

    @NotBlank(message = "课程title不能为空")
    private String title;             // 课程名称

    @Valid
    @Embedded
    @NotNull(message = "讲师信息不能为空")
    private Instructor instructor;    // 讲师信息
    @Embedded
    @NotNull(message = "课程信息不能为空")
    private Schedule schedule;    // 课程安排

    @Getter
    @NotNull(message = "课程容量不能为空")
    @Positive(message = "课程容量必须为正数")
    private Integer capacity;         // 课程容量
    @Getter
    @PositiveOrZero(message = "已选人数不能为负数")
    private Integer enrolled=0;         // 已选人数（默认0）
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 自动填充创建时间（新增时触发）
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (enrolled > capacity) {
            throw new IllegalArgumentException("已选人数不能超过课程容量（" + capacity + "）");
        }
    }

    public Course() {

    }

    @PreUpdate
    protected void validateEnrolled() {
        if (enrolled > capacity) {
            throw new IllegalArgumentException("已选人数不能超过课程容量（" + capacity + "）");
        }
    }
    // 初始化已选人数为0

    public Course( String code, String title, Instructor instructor, Schedule schedule, Integer capacity) {
        this.code = code;
        this.instructor =instructor;
        this.schedule =schedule;
        this.title = title;
        this.capacity = capacity;
        this.enrolled = 0;
    }
}

