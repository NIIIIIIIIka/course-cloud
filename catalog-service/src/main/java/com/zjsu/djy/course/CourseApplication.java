package com.zjsu.djy.course;
import com.zjsu.djy.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 应用启动类
 */
@SpringBootApplication
@RequiredArgsConstructor
public class CourseApplication {
    private final CourseService courseService;

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