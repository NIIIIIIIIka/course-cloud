//package com.zjgsu.djy.coursecloud.catalog.controller;
//
//import com.zjgsu.djy.coursecloud.catalog.dto.CourseDto;
//import com.zjgsu.djy.coursecloud.catalog.model.Course;
//import com.zjgsu.djy.coursecloud.catalog.service.CourseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Optional;
//
///**
// * Feign调用适配控制器（专门给enrollment-service调用，完全不修改原有CatalogController）
// * 路径与EnrollmentService的CatalogClient完全对齐
// */
//@RestController
//@RequestMapping("/api/courses") // 与原有CatalogController的根路径一致，保证Feign路径匹配
//public class FeignCourseController {
//
//    private final CourseService courseService;
//
//    @Autowired
//    public FeignCourseController(CourseService courseService) {
//        this.courseService = courseService;
//    }
//
//    /**
//     * Feign调用专用接口：根据课程ID查询，返回CourseDto（而非原生Course实体）
//     * 路径：/api/courses/{id}（与CatalogClient定义完全一致）
//     */
//    @Value("${server.port}")
//    private String serverPort;
//
//    @GetMapping("/{id}")
//    public ResponseEntity<CourseDto> getCourseForFeign(@PathVariable String id) {
//        // 复用原有CourseService逻辑，保证数据一致性
//        Optional<Course> courseOptional = courseService.getCourseById(id);
//
//        // 课程不存在则返回404（Feign会捕获404异常）
//        if (courseOptional.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // 转换原生Course实体 → CourseDto（屏蔽敏感字段，仅返回必要信息）
//        Course course = courseOptional.get();
//        CourseDto dto = new CourseDto();
//        dto.setCourseId(course.getId()); // 假设Course实体的主键是id（对应课程ID）
//        dto.setCourseName(course.getTitle()); // 课程名称
//        dto.setCourseCode(course.getCode()); // 课程编码
//
//        return ResponseEntity.ok(dto);
//    }
//}