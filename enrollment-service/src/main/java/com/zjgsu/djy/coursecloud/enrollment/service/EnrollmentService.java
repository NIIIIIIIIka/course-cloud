package com.zjgsu.djy.coursecloud.enrollment.service;

import com.zjgsu.djy.coursecloud.enrollment.client.CatalogClient;
import com.zjgsu.djy.coursecloud.enrollment.client.ServiceUnavailableException;
import com.zjgsu.djy.coursecloud.enrollment.client.UserClient;
import com.zjgsu.djy.coursecloud.enrollment.config.ServiceConfig;
import com.zjgsu.djy.coursecloud.enrollment.dto.CourseDto;
import com.zjgsu.djy.coursecloud.enrollment.dto.EnrollmentRequest;
import com.zjgsu.djy.coursecloud.enrollment.dto.StudentDto;
import com.zjgsu.djy.coursecloud.enrollment.exception.BadRequestException;
import com.zjgsu.djy.coursecloud.enrollment.exception.ResourceNotFoundException;
import com.zjgsu.djy.coursecloud.enrollment.model.Enrollment;
import com.zjgsu.djy.coursecloud.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserClient userClient;
    private final CatalogClient catalogClient;
    private final ServiceConfig serviceConfig;

    /**
     * 获取所有选课记录
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     * 根据课程ID查询选课记录
     */
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        // 验证课程是否存在
        validateCourseExists(courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    /**
     * 根据学生ID查询选课记录
     */
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        // 验证学生是否存在
        validateStudentExists(studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    /**
     * 学生选课
     */
    @Transactional
    public Enrollment enrollCourse(EnrollmentRequest request) {
        String courseId = request.getCourseId();
        String studentId = request.getStudentId();

        // 验证学生是否存在
        validateStudentExists(studentId);

        // 验证课程是否存在
        validateCourseExists(courseId);

        // 检查是否已经选过该课程
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            Enrollment existing = enrollmentRepository.findByCourseIdAndStudentId(courseId, studentId);

            if (existing.getStatus() == Enrollment.EnrollmentStatus.ACTIVE) {

                throw new BadRequestException("该学生已经选过此课程");
            }
            // 如果之前退过课，可以重新选课
            if (existing.getStatus() == Enrollment.EnrollmentStatus.DROPPED) {

                existing.setStatus(Enrollment.EnrollmentStatus.ACTIVE);

                existing.setEnrollTime(LocalDateTime.now());

                return enrollmentRepository.save(existing);
            }
        }

        // 创建新的选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setId(UUID.randomUUID().toString());
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setEnrollTime(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }

    /**
     * 学生退课
     */
    @Transactional
    public void dropCourse(String id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("选课记录不存在: " + id));

        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.DROPPED) {
            throw new BadRequestException("该选课记录已经退课");
        }

        if (enrollment.getStatus() == Enrollment.EnrollmentStatus.COMPLETED) {
            throw new BadRequestException("已完成的课程不能退课");
        }

        enrollment.setStatus(Enrollment.EnrollmentStatus.DROPPED);
        enrollmentRepository.save(enrollment);
    }

    /**
     * 验证学生是否存在
     */
    private void validateStudentExists(String studentId) {
        try {
            // Feign调用：返回StudentDto则代表存在，抛出异常则代表不存在/服务不可用
            StudentDto studentDto = userClient.getStudent(studentId);
            // 补充：如果远程服务返回null（极端情况），也判定为不存在
            if (studentDto == null) {
                throw new ResourceNotFoundException("studentDto为空，学生不存在: " + studentId);
            }
        } catch (ServiceUnavailableException e) {
            // 捕获降级异常，转抛友好提示
            log.error("用户服务不可用：{}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            // 捕获其他异常（如网络异常、序列化异常等）
            log.error("验证学生存在性时发生错误: {}", e.getMessage(), e);
            // 优先判断是否是「学生不存在」（Feign会把404转为FeignException，需解析）
            if (e.getMessage().contains("404") || e.getMessage().contains("Not Found")) {
                throw new ResourceNotFoundException("验证学生存在性时发生错误,学生不存在: " + studentId);
            }
            throw new BadRequestException("无法验证学生信息，请稍后重试");
        }
    }
    /**
     * 验证课程是否存在
     */
    private void validateCourseExists(String courseId) {
        try {
            // Feign调用：返回CourseDto则代表存在
            CourseDto courseDto = catalogClient.getCourse(courseId);
            if (courseDto == null) {
                throw new ResourceNotFoundException("课程不存在: " + courseId);
            }
        } catch (ServiceUnavailableException e) {
            log.error("课程服务不可用：{}", e.getMessage());
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            log.error("验证课程存在性时发生错误: {}", e.getMessage(), e);
            if (e.getMessage().contains("404") || e.getMessage().contains("Not Found")) {
                throw new ResourceNotFoundException("课程不存在: " + courseId);
            }
            throw new BadRequestException("无法验证课程信息，请稍后重试");
        }
    }
}
