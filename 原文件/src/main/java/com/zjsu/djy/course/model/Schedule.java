package com.zjsu.djy.course.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 嵌入式排课信息（无独立表，嵌入course表）
 */
@Data
@Embeddable // 标记为可嵌入式对象
public class Schedule {
    @NotBlank(message = "星期不能为空")
    @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$",
            message = "星期格式不正确，需为MONDAY/TUESDAY等")
    @Column(name = "schedule_day_of_week", nullable = false)
    private String dayOfWeek; // 星期（MONDAY/TUESDAY等）

    @NotBlank(message = "开始时间不能为空")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "开始时间格式不正确，需为HH:mm（如08:30）")
    @Column(name = "schedule_start_time", nullable = false)
    private String startTime; // 开始时间（HH:mm）

    @NotBlank(message = "结束时间不能为空")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "结束时间格式不正确，需为HH:mm（如09:40）")
    @Column(name = "schedule_end_time", nullable = false)
    private String endTime; // 结束时间（HH:mm）


    private Integer expectedAttendance;
}