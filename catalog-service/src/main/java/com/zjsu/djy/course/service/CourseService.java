package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.InvalidParameterException;
import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.exception.BusinessConflictException;
import com.zjsu.djy.course.model.Course;
import com.zjsu.djy.course.model.Instructor;
import com.zjsu.djy.course.model.Schedule;
import com.zjsu.djy.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    // 查询所有课程（略）
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 根据ID查询课程（不存在则抛404）
    public Course getCourseById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    // 创建课程
    @Transactional
    public Course createCourse(Course course) {
        // 1. 参数校验（非法参数抛400）
        validateCourseForCreation(course);

        // 2. 初始化默认值
        if (course.getEnrolled() == null) {
            course.setEnrolled(0);
        }
        course.setCreatedAt(LocalDateTime.now());

        // 3. 保存课程
        return courseRepository.save(course);
    }

    // 更新课程
    @Transactional
    public Course updateCourse(String id, Course course) {
        // 1. 校验原课程存在（不存在抛404）
        getCourseById(id);

        // 2. 锁定ID和创建时间
        course.setId(id);
        course.setCreatedAt(getCourseById(id).getCreatedAt());

        // 3. 参数校验（非法参数抛400）
        validateCourseForUpdate(course);

        // 4. 执行更新
        return courseRepository.save(course);
    }

    // 删除课程
    @Transactional
    public void deleteCourse(String id) {
        // 1. 校验课程存在（不存在抛404）
        Course course = getCourseById(id);

        // 2. 业务冲突：已选学生的课程不能删除（抛409）
        if (course.getEnrolled() > 0) {
            throw new BusinessConflictException("Cannot delete course with enrolled students (id: " + id + ")");
        }

        // 3. 执行删除
        courseRepository.deleteById(id);
    }

    // 选课：增加已选人数
    @Transactional
    public void incrementEnrolledCount(String courseId) {
        Course course = getCourseById(courseId);

        // 业务冲突：课程已满（抛409）
        if (course.getEnrolled() >= course.getCapacity()) {
            throw new BusinessConflictException("Course " + courseId + " is full (capacity: " + course.getCapacity() + ")");
        }

        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);
    }

    // 退课：减少已选人数
    @Transactional
    public void decrementEnrolledCount(String courseId) {
        Course course = getCourseById(courseId);

        // 业务冲突：已选人数不能为负（抛409）
        if (course.getEnrolled() <= 0) {
            throw new BusinessConflictException("Enrolled count cannot be negative (course id: " + courseId + ")");
        }

        course.setEnrolled(course.getEnrolled() - 1);
        courseRepository.save(course);
    }

    // ------------------------------ 私有校验方法 ------------------------------

    // 创建课程时的参数校验（非法参数抛400）
    private void validateCourseForCreation(Course course) {
        // 核心字段非空校验
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new InvalidParameterException("Course code cannot be empty");
        }
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new InvalidParameterException("Course title cannot be empty");
        }
        if (course.getCapacity() == null || course.getCapacity() <= 0) {
            throw new InvalidParameterException("Capacity must be a positive number");
        }

        // 嵌入式对象Instructor校验
        Instructor instructor = course.getInstructor();
        if (instructor == null) {
            throw new InvalidParameterException("Instructor information is required");
        }
        if (instructor.getName() == null || instructor.getName().trim().isEmpty()) {
            throw new InvalidParameterException("Instructor name cannot be empty");
        }
        if (instructor.getEmail() == null || instructor.getEmail().trim().isEmpty()) {
            throw new InvalidParameterException("Instructor email cannot be empty");
        }

        // 嵌入式对象Schedule校验
        Schedule schedule = course.getSchedule();
        if (schedule == null) {
            throw new InvalidParameterException("Schedule information is required");
        }
        if (schedule.getDayOfWeek() == null || schedule.getDayOfWeek().trim().isEmpty()) {
            throw new InvalidParameterException("Day of week cannot be empty");
        }
        if (schedule.getStartTime() == null || schedule.getStartTime().trim().isEmpty()) {
            throw new InvalidParameterException("Start time cannot be empty");
        }
        if (schedule.getEndTime() == null || schedule.getEndTime().trim().isEmpty()) {
            throw new InvalidParameterException("End time cannot be empty");
        }

        // 课程编码唯一性校验（重复编码属于业务冲突，抛409）
        if (courseRepository.existsByCode(course.getCode())) {
            throw new BusinessConflictException("Course code already exists: " + course.getCode());
        }
    }

    // 更新课程时的参数校验
    private void validateCourseForUpdate(Course course) {
        // 核心字段非空校验（抛400）
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new InvalidParameterException("Course code cannot be empty");
        }
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new InvalidParameterException("Course title cannot be empty");
        }
        if (course.getCapacity() == null || course.getCapacity() <= 0) {
            throw new InvalidParameterException("Capacity must be a positive number");
        }

        // 容量小于已选人数（业务冲突，抛409）
        if (course.getCapacity() < course.getEnrolled()) {
            throw new BusinessConflictException("Capacity cannot be less than enrolled count (capacity: " + course.getCapacity() + ", enrolled: " + course.getEnrolled() + ")");
        }

        // 嵌入式对象非空校验（抛400）
        if (course.getInstructor() == null || course.getInstructor().getName() == null) {
            throw new InvalidParameterException("Instructor name cannot be empty");
        }
        if (course.getSchedule() == null || course.getSchedule().getDayOfWeek() == null) {
            throw new InvalidParameterException("Day of week cannot be empty");
        }
    }
}