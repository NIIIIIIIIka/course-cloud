package com.zjsu.djy.course.exception;

/**
 * 资源不存在异常（404）
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}