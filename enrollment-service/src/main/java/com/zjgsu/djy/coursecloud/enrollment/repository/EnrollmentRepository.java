package com.zjgsu.djy.coursecloud.enrollment.repository;

import com.zjgsu.djy.coursecloud.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
    
    /**
     * 根据课程ID查询选课记录
     */
    List<Enrollment> findByCourseId(String courseId);
    
    /**
     * 根据学生ID查询选课记录
     */
    List<Enrollment> findByStudentId(String studentId);
    
    /**
     * 根据课程ID和学生ID查询选课记录
     */
    Enrollment findByCourseIdAndStudentId(String courseId, String studentId);
    
    /**
     * 检查是否存在指定课程和学生的选课记录
     */
    boolean existsByCourseIdAndStudentId(String courseId, String studentId);
}

