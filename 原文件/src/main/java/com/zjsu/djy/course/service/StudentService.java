package com.zjsu.djy.course.service;

import com.zjsu.djy.course.exception.BusinessConflictException;
import com.zjsu.djy.course.exception.InvalidParameterException;
import com.zjsu.djy.course.exception.ResourceNotFoundException;
import com.zjsu.djy.course.model.Student;
import com.zjsu.djy.course.repository.EnrollmentRepository;
import com.zjsu.djy.course.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 学生业务逻辑：实现学生管理API及删除校验规则（1-44）
 */
@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository; // 用于删除前选课记录检查

    // 邮箱格式正则（提取为常量，避免重复定义）
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 查询所有学生：对应文档1-32 GET /api/students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * 根据ID查询学生：不存在则抛404
     */
    public Student getStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    /**
     * 创建学生：校验邮箱格式+学号唯一性，参数非法抛400，冲突抛409
     */
    @Transactional
    public Student createStudent(Student student) {
        // 1. 基础参数校验（非法参数抛400）
        validateStudentForCreation(student);

        // 2. 校验学号唯一性（冲突抛409，文档1-31）
        studentRepository.findIdByStudentId(student.getStudentId())
                .ifPresent(id -> {
                    throw new BusinessConflictException("StudentId already exists: " + student.getStudentId());
                });

        // 3. 保存学生（Repository自动生成ID和createdAt）
        return studentRepository.save(student);
    }

    /**
     * 更新学生：校验学号唯一性+不可修改ID/createdAt
     */
    @Transactional
    public Student updateStudent(String id, Student student) {
        // 1. 校验原学生存在（不存在抛404）
        Student existingStudent = getStudentById(id);

        // 2. 锁定系统生成字段（不可修改）
        student.setId(id);
        student.setCreatedAt(existingStudent.getCreatedAt());

        // 3. 基础参数校验（非法参数抛400）
        validateStudentForUpdate(student);

        // 4. 校验学号唯一性（若学号变更，冲突抛409）
        if (!existingStudent.getStudentId().equals(student.getStudentId())) {
            studentRepository.findIdByStudentId(student.getStudentId())
                    .ifPresent(existingId -> {
                        // 若存在其他学生使用该学号，抛冲突
                        if (!existingId.equals(id)) {
                            throw new BusinessConflictException("StudentId already exists: " + student.getStudentId());
                        }
                    });
        }

        // 5. 更新学生
        return studentRepository.save(student);
    }

    /**
     * 删除学生：校验是否有选课记录，有则抛409
     */
    @Transactional
    public void deleteStudent(String id) {
        // 1. 校验学生存在（不存在抛404）
        getStudentById(id);

        // 2. 校验是否有选课记录（有则冲突抛409，文档1-44）
        if (!enrollmentRepository.findByStudentId(id).isEmpty()) {
            throw new BusinessConflictException("无法删除：该学生存在选课记录（studentId: " + id + "）");
        }

        // 3. 执行删除
        studentRepository.deleteById(id);
    }

    // ------------------------------ 私有校验方法 ------------------------------

    /**
     * 创建学生时的参数校验（非法参数抛400）
     */
    private void validateStudentForCreation(Student student) {
        if (student == null) {
            throw new InvalidParameterException("学生信息不能为空");
        }

        // 学号非空校验
        String studentId = student.getStudentId();
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new InvalidParameterException("学号（studentId）不能为空");
        }

        // 姓名非空校验
        String name = student.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidParameterException("学生姓名不能为空");
        }

        // 邮箱格式校验
        String email = student.getEmail();
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidParameterException("邮箱不能为空");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidParameterException("邮箱格式无效: " + email + "（示例：xxx@example.com）");
        }
    }

    /**
     * 更新学生时的参数校验（复用创建时的校验，排除无需重复校验的字段）
     */
    private void validateStudentForUpdate(Student student) {
        // 复用创建时的核心参数校验（学号、姓名、邮箱）
        validateStudentForCreation(student);

        // 额外的更新校验（如有需要）
        // 例如：禁止修改某些特殊字段（如角色、状态等，根据业务补充）
    }
}