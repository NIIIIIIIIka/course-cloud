package com.zjsu.djy.course.model;

import com.zjsu.djy.course.enums.EnrollmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 选课记录实体
 */
@Getter
@Data
@Entity
@Table(name = "enrollment",
        uniqueConstraints = { @UniqueConstraint(
                columnNames = {"student_id", "course_id"}, // 数据库列名（外键列）
                name = "uk_student_course" // 约束名称（可选，方便后续维护）
        )})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;         // 选课记录ID（系统生成UUID）
    @Column(name = "course_id", nullable = false, updatable = false)
    private String courseId;   // 课程ID

    @Column(name = "student_id", nullable = false, updatable = false)
    private String studentId;  // 学生ID

    @Column(name = "enroll_time", nullable = false, updatable = false)
    private LocalDateTime enrollTime;

    // 4. 选课状态：使用枚举，确保状态值规范
    @NotNull(message = "选课状态不能为空")
    @Enumerated(EnumType.STRING)  // 数据库存储枚举的code（如ACTIVE），可读性更高
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @PrePersist
    protected void onCreate() {
        this.enrollTime = LocalDateTime.now();
        // 默认状态为“正常”
        if (this.status == null) {
            this.status = EnrollmentStatus.ACTIVE;
        }
    }

    // 退课操作：状态改为DROPPED并记录退课时间（业务方法，可放Service层）
    public void drop() {
        this.status = EnrollmentStatus.DROPPED;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}