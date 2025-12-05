package com.zjsu.djy.course.exception;

/**
 * 业务冲突异常：用于操作违反唯一性约束、资源状态冲突等场景
 * 对应HTTP状态码：409 Conflict（如重复选课、课程编码重复）
 */
public class BusinessConflictException extends RuntimeException {
    public BusinessConflictException(String message) {
        super(message);
    }
}