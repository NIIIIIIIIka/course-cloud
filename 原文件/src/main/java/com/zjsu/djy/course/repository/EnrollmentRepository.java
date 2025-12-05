package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.enums.EnrollmentStatus;
import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 选课数据访问：基于ConcurrentHashMap的内存存储，遵循文档1-53至1-65选课管理API
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,String> {


    public List<Enrollment> findByCourseId(String courseId);

    public List<Enrollment> findByStudentId(String studentId);
    List<Enrollment> findByCourseIdAndStudentIdAndStatus(
            String courseId,
            String studentId,
            EnrollmentStatus status
    );
    long countByCourseIdAndStatus(String courseId, EnrollmentStatus status);
    public boolean existsByCourseIdAndStudentId(String courseId, String studentId) ;
    boolean existsByCourseIdAndStudentIdAndStatus(
            String courseId,
            String studentId,
            EnrollmentStatus status
    );
    default boolean existsActiveByCourseIdAndStudentId(String courseId, String studentId) {
        return existsByCourseIdAndStudentIdAndStatus(courseId, studentId, EnrollmentStatus.ACTIVE);
    }

}