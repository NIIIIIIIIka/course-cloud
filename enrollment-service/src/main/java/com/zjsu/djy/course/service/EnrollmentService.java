package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.BusinessConflictException;
import com.zjsu.djy.course.exception.BusinessException;
import com.zjsu.djy.course.exception.InvalidParameterException;
import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.model.Enrollment;
import com.zjsu.djy.course.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

/**
 * 选课业务逻辑：实现选课管理API及所有业务规则（1-68至1-73）
 */



@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final RestTemplate restTemplate;     // 课程校验+容量检查+级联更新
    private final StudentService studentService;   // 学生校验

    @Value("${catalog-service.url}")
    private String catalogServiceUrl;

    /**
     * 查询所有选课记录：对应文档1-60 GET /api/enrollments
     */
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    /**
     * 根据ID查询选课记录：对应文档1-59 DELETE前的前置查询
     */
    public Enrollment getEnrollmentById(String id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    /**
     * 按课程查询选课记录：对应文档1-62 GET /api/enrollments/course/{courseId}
     */
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        // 校验课程存在（不存在抛404）
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException(courseId);
        }
        // 查不到记录时返回空列表（而非404，符合查询类接口的常规设计）
        return enrollmentRepository.findByCourseId(courseId);
    }

    /**
     * 按学生查询选课记录：对应文档1-64 GET /api/enrollments/student/{studentId}
     */
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        // 校验学生存在（不存在抛404）
        studentService.getStudentById(studentId);
        // 查不到记录时返回空列表
        return enrollmentRepository.findByStudentId(studentId);
    }

    /**
     * 学生选课：对应文档1-55 POST /api/enrollments，实现1-68至1-73所有业务规则
     */

    @Transactional(rollbackFor = Exception.class) // 确保选课+人数更新原子性，异常时回滚
    public Enrollment enrollCourse(Enrollment enrollment) {
        // 1. 前置参数校验（非法参数抛400）
        validateEnrollmentParams(enrollment);
        String courseId = enrollment.getCourseId();
        String studentId = enrollment.getStudentId();

        // 2.1 校验学生存在（不存在抛404）
        studentService.getStudentById(studentId);
        // 2.2 调用课程目录服务验证课程是否存在
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException(courseId);
        }
        // 3. 从响应中提取课程信息
        Map<String, Object> courseData = (Map<String, Object>)courseResponse.get("data");
        Integer capacity = (Integer) courseData.get("capacity");
        Integer enrolled = (Integer) courseData.get("enrolled");
        // 4. 检查课程容量
        if (enrolled >= capacity) {
            throw new BusinessException("无法选课：该课程选课人数已满Course is full");
        }


        // 5. 校验重复选课（业务冲突抛409，文档1-69）
        if (enrollmentRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new BusinessConflictException("无法选课：学生已选该课程（courseId: " + courseId + ", studentId: " + studentId + "）");
        }




        // 6. 保存选课记录（Repository自动生成ID）
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 7. 级联更新：课程已选人数+1（文档1-72，若更新失败则整体回滚）
        updateCourseEnrolledCount(courseId, enrolled + 1);

        return savedEnrollment;
    }
    private void updateCourseEnrolledCount(String courseId, int newCount) {
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> updateData = Map.of("enrolled", newCount);
        try {
            restTemplate.put(url, updateData);
        } catch (Exception e) {
            // 记录日志但不影响主流程
            System.err.println("Failed to update course enrolled count: " +
                    e.getMessage());
        }
    }
    /**
     * 学生退课：对应文档1-59 DELETE /api/enrollments/{id}，级联更新课程人数
     */
    @Transactional(rollbackFor = Exception.class)
    public void dropCourse(String enrollmentId) {
        // 1. 校验选课记录存在（不存在抛404）
        Enrollment enrollment = getEnrollmentById(enrollmentId);
        String courseId = enrollment.getCourseId();

        // 2. 先删除选课记录（避免后续更新人数失败导致“记录已删但人数未减”的不一致）
        enrollmentRepository.deleteById(enrollmentId);

        // 3. 级联更新：课程已选人数-1（文档1-72，若更新失败则删除操作回滚）
//        String url = catalogServiceUrl + "/api/courses/" + courseId;
//        Map<String, Object> courseResponse;
//        try {
//            courseResponse = restTemplate.getForObject(url, Map.class);
//        } catch (HttpClientErrorException.NotFound e) {
//            throw new ResourceNotFoundException(courseId);
//        }
        Map<String, Object> courseData = getCourseInfo(courseId);
        Integer currentEnrolled = (Integer) courseData.get("enrolled");
        int newEnrolled = Math.max(currentEnrolled - 1, 0);
        updateCourseEnrolledCount(courseId, newEnrolled);

    }

    // ------------------------------ 私有校验方法 ------------------------------

    /**
     * 选课参数前置校验：确保核心参数非空（非法参数抛400）
     */
    private void validateEnrollmentParams(Enrollment enrollment) {
        if (enrollment == null) {
            throw new InvalidParameterException("选课参数不能为空");
        }
        // 校验courseId非空
        if (enrollment.getCourseId() == null || enrollment.getCourseId().trim().isEmpty()) {
            throw new InvalidParameterException("课程ID不能为空");
        }
        // 校验studentId非空
        if (enrollment.getStudentId() == null || enrollment.getStudentId().trim().isEmpty()) {
            throw new InvalidParameterException("学生ID不能为空");
        }
    }
    private Map<String, Object> getCourseInfo(String courseId) {
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            return (Map<String, Object>) response.get("data");
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        } catch (Exception e) {
            throw new BusinessConflictException("课程服务暂时不可用，请稍后重试");
        }
    }
}