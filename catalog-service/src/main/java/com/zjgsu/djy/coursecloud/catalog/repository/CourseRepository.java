package com.zjgsu.djy.coursecloud.catalog.repository;

import com.zjgsu.djy.coursecloud.catalog.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
    /**
     * 根据课程编码查询课程
     * @param code 课程编码
     * @return 课程对象
     */
    Optional<Course> findByCode(String code);
    
    /**
     * 检查课程编码是否存在
     * @param code 课程编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查讲师邮箱是否存在
     * @param email 讲师邮箱
     * @return 是否存在
     */
    boolean existsByInstructorEmail(String email);
}

