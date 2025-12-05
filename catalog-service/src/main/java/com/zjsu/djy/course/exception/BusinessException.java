package com.zjsu.djy.course.exception;

/**
 * 业务逻辑异常：对应文档1-84错误响应（code=400）和1-91状态码定义
 * 如课程容量已满、重复选课、学生有选课记录无法删除等（文档1-68至1-73）
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}