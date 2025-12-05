package com.zjgsu.djy.coursecloud.catalog.service;

import com.zjgsu.djy.coursecloud.catalog.dto.CourseRequest;
import com.zjgsu.djy.coursecloud.catalog.model.Course;
import com.zjgsu.djy.coursecloud.catalog.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    
    private final CourseRepository courseRepository;
    
    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    
    /**
     * 获取所有课程
     * @return 课程列表
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
    /**
     * 根据ID获取课程
     * @param id 课程ID
     * @return 课程对象
     */
    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }
    
    /**
     * 根据课程编码获取课程
     * @param code 课程编码
     * @return 课程对象
     */
    public Optional<Course> getCourseByCode(String code) {
        return courseRepository.findByCode(code);
    }
    
    /**
     * 创建课程
     * @param courseRequest 课程请求对象
     * @return 创建的课程对象
     * @throws IllegalArgumentException 如果课程编码或讲师邮箱已存在
     */
    public Course createCourse(CourseRequest courseRequest) {
        // 检查课程编码是否已存在
        if (courseRepository.existsByCode(courseRequest.getCode())) {
            throw new IllegalArgumentException("课程编码已存在: " + courseRequest.getCode());
        }
        
        // 检查讲师邮箱是否已存在
        if (courseRepository.existsByInstructorEmail(courseRequest.getInstructorEmail())) {
            throw new IllegalArgumentException("讲师邮箱已存在: " + courseRequest.getInstructorEmail());
        }
        
        // 验证已选人数不能超过容量
        Integer enrolled = courseRequest.getEnrolled() != null ? courseRequest.getEnrolled() : 0;
        if (enrolled > courseRequest.getCapacity()) {
            throw new IllegalArgumentException("已选人数不能超过课程容量");
        }
        
        Course course = new Course();
        course.setCode(courseRequest.getCode());
        course.setTitle(courseRequest.getTitle());
        course.setCapacity(courseRequest.getCapacity());
        course.setEnrolled(enrolled);
        course.setInstructorId(courseRequest.getInstructorId());
        course.setInstructorName(courseRequest.getInstructorName());
        course.setInstructorEmail(courseRequest.getInstructorEmail());
        course.setScheduleDayOfWeek(courseRequest.getScheduleDayOfWeek());
        course.setScheduleStartTime(courseRequest.getScheduleStartTime());
        course.setScheduleEndTime(courseRequest.getScheduleEndTime());
        course.setExpectedAttendance(courseRequest.getExpectedAttendance());
        
        return courseRepository.save(course);
    }
    
    /**
     * 更新课程
     * @param id 课程ID
     * @param courseRequest 课程请求对象
     * @return 更新后的课程对象
     * @throws IllegalArgumentException 如果课程不存在或数据验证失败
     */
    public Course updateCourse(String id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("课程不存在: " + id));
        
        // 如果修改了课程编码，检查新编码是否已存在
        if (!course.getCode().equals(courseRequest.getCode()) && 
            courseRepository.existsByCode(courseRequest.getCode())) {
            throw new IllegalArgumentException("课程编码已存在: " + courseRequest.getCode());
        }
        
        // 如果修改了讲师邮箱，检查新邮箱是否已存在
        if (!course.getInstructorEmail().equals(courseRequest.getInstructorEmail()) && 
            courseRepository.existsByInstructorEmail(courseRequest.getInstructorEmail())) {
            throw new IllegalArgumentException("讲师邮箱已存在: " + courseRequest.getInstructorEmail());
        }
        
        // 验证已选人数不能超过容量
        Integer enrolled = courseRequest.getEnrolled() != null ? courseRequest.getEnrolled() : course.getEnrolled();
        if (enrolled > courseRequest.getCapacity()) {
            throw new IllegalArgumentException("已选人数不能超过课程容量");
        }
        
        course.setCode(courseRequest.getCode());
        course.setTitle(courseRequest.getTitle());
        course.setCapacity(courseRequest.getCapacity());
        course.setEnrolled(enrolled);
        course.setInstructorId(courseRequest.getInstructorId());
        course.setInstructorName(courseRequest.getInstructorName());
        course.setInstructorEmail(courseRequest.getInstructorEmail());
        course.setScheduleDayOfWeek(courseRequest.getScheduleDayOfWeek());
        course.setScheduleStartTime(courseRequest.getScheduleStartTime());
        course.setScheduleEndTime(courseRequest.getScheduleEndTime());
        course.setExpectedAttendance(courseRequest.getExpectedAttendance());
        
        return courseRepository.save(course);
    }
    
    /**
     * 删除课程
     * @param id 课程ID
     * @throws IllegalArgumentException 如果课程不存在
     */
    public void deleteCourse(String id) {
        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("课程不存在: " + id);
        }
        courseRepository.deleteById(id);
    }
}

