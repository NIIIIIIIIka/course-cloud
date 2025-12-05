package com.zjgsu.djy.coursecloud.enrollment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"course_id", "student_id"}, name = "UK_course_student")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    
    @Id
    @Column(name = "id", length = 255)
    private String id;
    
    @Column(name = "course_id", nullable = false, length = 36)
    private String courseId;
    
    @Column(name = "student_id", nullable = false, length = 36)
    private String studentId;
    
    @Column(name = "enroll_time", nullable = false)
    private LocalDateTime enrollTime;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EnrollmentStatus status;
    
    public enum EnrollmentStatus {
        ACTIVE,
        COMPLETED,
        DROPPED,
        EXPIRED
    }
}

