package com.zjsu.djy.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器：严格遵循文档1-81至1-85的统一响应格式
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源不存在异常（404）：对应文档1-84示例（Course not found）
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.NOT_FOUND.value()); // 404，文档1-92
        response.put("message", ex.getMessage());
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * 处理业务逻辑异常（400）：如文档1-68（容量限制）、1-69（重复选课）
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value()); // 400，文档1-91
        response.put("message", ex.getMessage());
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数校验异常（400）：如实体类中@NotBlank/@Pattern校验失败（文档1-131/1-132）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value()); // 400，文档1-91
        response.put("message", "参数校验失败：" + errors);
        response.put("data", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}