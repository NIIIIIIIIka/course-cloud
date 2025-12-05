package com.zjsu.djy.course.repository;

import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.service.CourseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 课程数据访问：基于ConcurrentHashMap的内存存储，完全遵循文档1-94/1-97要求
 */
@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
    // 线程安全的Map存储课程（key=课程ID，文档1-97示例）

    /**
     * 统计/筛选有剩余容量的课程
     * （假设Course实体中有totalCapacity总容量和currentEnrollment当前报名人数字段）
     *
     * @return 剩余容量>0的课程列表
     */
    List<Course> findByCodeAndInstructorTeacherId(String code, String teacherId);
    @Query("SELECT c FROM Course c WHERE c.enrolled < c.capacity")
    List<Course> findCoursesWithEnrolledLessThanCapacity();
//    List<Course> findByEnrolledLessThanCapacity();
    List<Course> findByCode(String code);
    List<Course> findByInstructorTeacherId(String teacherId);
    /**
     * 按标题关键字模糊查询课程
     * （忽略大小写，匹配标题中包含关键字的课程）
     *
     * @param keyword 标题关键字
     * @return 符合条件的课程列表
     */
    List<Course> findByTitleContainingIgnoreCase(String keyword);
    boolean existsByCode(String code);
//    public Course createCourse(Course course){
//        String id=UUID.randomUUID().toString();
//        course.setId(id);
//        courses.put(id, course);
//        return course;
//    }
    /**
     * 根据ID删除课程：对应文档1-24 DELETE /api/courses/{id}
     */

}