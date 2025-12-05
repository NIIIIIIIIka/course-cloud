package com.zjgsu.djy.coursecloud.enrollment.service;

import com.zjgsu.djy.coursecloud.enrollment.config.ServiceConfig;
import com.zjgsu.djy.coursecloud.enrollment.dto.EnrollmentRequest;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;
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
            // 使用服务名进行调用，支持Nacos服务发现和负载均衡
            String url = "http://user-service/api/students/" + studentId;
            log.debug("调用用户服务验证学生: {}", url);
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ResourceNotFoundException("学生不存在: " + studentId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("学生不存在: " + studentId);
        } catch (Exception e) {
            log.error("验证学生存在性时发生错误: {}", e.getMessage());
            throw new BadRequestException("无法验证学生信息，请稍后重试");
        }
    }

    /**
     * 验证课程是否存在
     */
    private void validateCourseExists(String courseId) {
        try {
            // 使用服务名进行调用，支持Nacos服务发现和负载均衡
            String url = "http://catalog-service/api/courses/" + courseId;
            log.debug("调用课程目录服务验证课程: {}", url);
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new ResourceNotFoundException("课程不存在: " + courseId);
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("课程不存在: " + courseId);
        } catch (Exception e) {
            log.error("验证课程存在性时发生错误: {}", e.getMessage());
            throw new BadRequestException("无法验证课程信息，请稍后重试");
        }
    }
}
