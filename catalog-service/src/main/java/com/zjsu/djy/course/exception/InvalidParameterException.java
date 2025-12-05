package com.zjsu.djy.course.exception;

/**
 * 参数非法异常：用于请求参数格式错误、必填项缺失等场景
 * 对应HTTP状态码：400 Bad Request
 */
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message) {
        super(message);
    }
}