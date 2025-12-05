package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 学生数据访问：基于ConcurrentHashMap的内存存储，遵循文档1-26至1-44学生管理API
 */
@Repository
public interface  StudentRepository extends JpaRepository<Student, String> {
    // 线程安全的Map存储学生（key=学生ID，文档1-94内存存储）



    /**
     * 根据学号查询学生ID：用于学号唯一性校验（文档1-31）
     */
    public Optional<Student> findIdByStudentId(String studentId) ;
    public Optional<Student> findByStudentId(String studentId) ;
    Optional<Student> findByEmail(String email);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    List<Student> findByMajor(String major);
    List<Student> findByGrade(Integer grade);

    List<Student> findByMajorAndGrade(String major, Integer grade);
}