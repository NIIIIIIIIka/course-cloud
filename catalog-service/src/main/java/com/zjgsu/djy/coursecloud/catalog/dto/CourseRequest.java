package com.zjgsu.djy.coursecloud.catalog.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class CourseRequest implements Serializable {
    
    @NotBlank(message = "课程编码不能为空")
    private String code;
    
    @NotBlank(message = "课程名称不能为空")
    private String title;
    
    @NotNull(message = "课程容量不能为空")
    @Min(value = 1, message = "课程容量必须大于0")
    private Integer capacity;
    
    private Integer enrolled;
    
    @NotBlank(message = "讲师ID不能为空")
    private String instructorId;
    
    @NotBlank(message = "讲师姓名不能为空")
    private String instructorName;
    
    @NotBlank(message = "讲师邮箱不能为空")
    @Email(message = "讲师邮箱格式不正确")
    private String instructorEmail;
    
    @NotBlank(message = "排课星期不能为空")
    private String scheduleDayOfWeek;
    
    @NotBlank(message = "开始时间不能为空")
    private String scheduleStartTime;
    
    @NotBlank(message = "结束时间不能为空")
    private String scheduleEndTime;
    
    private Integer expectedAttendance;
    
    // Getters and Setters
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

