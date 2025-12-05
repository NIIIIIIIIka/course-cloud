package com.zjgsu.djy.coursecloud.catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course", 
       uniqueConstraints = {
           @UniqueConstraint(name = "UK_course_code", columnNames = "code"),
           @UniqueConstraint(name = "UK_instructor_email", columnNames = "instructor_email")
       })
public class Course {
    
    @Id
    @Column(name = "id", length = 36)
    private String id;
    
    @NotBlank(message = "课程编码不能为空")
    @Column(name = "code", nullable = false, unique = true, length = 255)
    private String code;
    
    @NotBlank(message = "课程名称不能为空")
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @NotNull(message = "课程容量不能为空")
    @Min(value = 1, message = "课程容量必须大于0")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "enrolled", nullable = false)
    private Integer enrolled = 0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @NotBlank(message = "讲师ID不能为空")
    @Column(name = "instructor_id", nullable = false, length = 255)
    private String instructorId;
    
    @NotBlank(message = "讲师姓名不能为空")
    @Column(name = "instructor_name", nullable = false, length = 255)
    private String instructorName;
    
    @NotBlank(message = "讲师邮箱不能为空")
    @Email(message = "讲师邮箱格式不正确")
    @Column(name = "instructor_email", nullable = false, unique = true, length = 255)
    private String instructorEmail;
    
    @NotBlank(message = "排课星期不能为空")
    @Column(name = "schedule_day_of_week", nullable = false, length = 255)
    private String scheduleDayOfWeek;
    
    @NotBlank(message = "开始时间不能为空")
    @Column(name = "schedule_start_time", nullable = false, length = 255)
    private String scheduleStartTime;
    
    @NotBlank(message = "结束时间不能为空")
    @Column(name = "schedule_end_time", nullable = false, length = 255)
    private String scheduleEndTime;
    
    @Column(name = "expected_attendance")
    private Integer expectedAttendance;
    
    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (enrolled == null) {
            enrolled = 0;
        }
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public Integer getEnrolled() {
        return enrolled;
    }
    
    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public String getInstructorEmail() {
        return instructorEmail;
    }
    
    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }
    
    public String getScheduleDayOfWeek() {
        return scheduleDayOfWeek;
    }
    
    public void setScheduleDayOfWeek(String scheduleDayOfWeek) {
        this.scheduleDayOfWeek = scheduleDayOfWeek;
    }
    
    public String getScheduleStartTime() {
        return scheduleStartTime;
    }
    
    public void setScheduleStartTime(String scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }
    
    public String getScheduleEndTime() {
        return scheduleEndTime;
    }
    
    public void setScheduleEndTime(String scheduleEndTime) {
        this.scheduleEndTime = scheduleEndTime;
    }
    
    public Integer getExpectedAttendance() {
        return expectedAttendance;
    }
    
    public void setExpectedAttendance(Integer expectedAttendance) {
        this.expectedAttendance = expectedAttendance;
    }
}

