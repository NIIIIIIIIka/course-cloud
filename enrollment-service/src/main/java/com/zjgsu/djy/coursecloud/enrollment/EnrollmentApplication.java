package com.zjgsu.djy.coursecloud.enrollment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients // 启用 Feign 客户端
@SpringBootApplication
public class EnrollmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnrollmentApplication.class, args);
    }
}

