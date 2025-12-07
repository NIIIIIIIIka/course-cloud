package com.zjgsu.djy.coursecloud.enrollment.client;

public class ServiceUnavailableException extends RuntimeException {
    public ServiceUnavailableException(String message) {
        super(message);
    }
}