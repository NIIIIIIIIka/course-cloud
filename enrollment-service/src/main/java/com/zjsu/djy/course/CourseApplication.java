package com.zjsu.djy.course;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 应用启动类
 */
@SpringBootApplication
@RequiredArgsConstructor
public class CourseApplication {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // 创建 RestTemplate 实例并返回
    }
    public static void main(String[] args) {

        SpringApplication.run(CourseApplication.class, args);
    }

    /**
     * 初始化测试数据（启动时执行）
     */
    @Bean
    CommandLineRunner initTestData() {
        return args -> {

        };
    }

}