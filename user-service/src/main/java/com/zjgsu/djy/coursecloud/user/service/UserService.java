package com.zjgsu.djy.coursecloud.user.service;

import com.zjgsu.djy.coursecloud.user.model.UpdateUserDTO;
import com.zjgsu.djy.coursecloud.user.model.User;
import com.zjgsu.djy.coursecloud.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 用户服务类
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final String USER_TYPE_STUDENT = "student";
    private static final String USER_TYPE_TEACHER = "teacher";
    /**
     * 获取所有学生
     */
    public List<User> getAllStudents() {
        return userRepository.findByUserType(USER_TYPE_STUDENT);
    }

    /**
     * 根据ID获取学生
     */
    public Optional<User> getStudentById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.filter(u -> USER_TYPE_STUDENT.equals(u.getUserType()));
    }

    /**
     * 根据学号获取学生
     */
    public Optional<User> getStudentByStudentId(String studentId) {
        return userRepository.findByUserIdAndUserType(studentId, USER_TYPE_STUDENT);
    }

    /**
     * 创建学生
     */
    public User createStudent(User user) {
        // 检查学号是否已存在
        if (userRepository.findByUserId(user.getUserId()).isPresent()) {
            throw new RuntimeException("学号已存在: " + user.getUserId());
        }

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("邮箱已存在: " + user.getEmail());
        }

        user.setId(UUID.randomUUID().toString());
        user.setUserType(USER_TYPE_STUDENT);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        return userRepository.save(user);
    }

    /**
     * 更新学生
     */
    public User updateStudent(String id, UpdateUserDTO userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));

        if (!USER_TYPE_STUDENT.equals(user.getUserType())) {
            throw new RuntimeException("ID对应的用户不是学生: " + id);
        }

        // 检查学号是否被其他用户使用
        if (!user.getUserId().equals(userDetails.getUserId())) {
            Optional<User> existingUser = userRepository.findByUserId(userDetails.getUserId());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("学号已被其他用户使用: " + userDetails.getUserId());
            }
        }

        // 检查邮箱是否被其他用户使用
        if (!user.getEmail().equals(userDetails.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(userDetails.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new RuntimeException("邮箱已被其他用户使用: " + userDetails.getEmail());
            }
        }

        if (userDetails.getUserId()!=null){
            user.setUserId(userDetails.getUserId());
        }
        if(userDetails.getName()!=null){
            user.setName(userDetails.getName());
        }
        if(userDetails.getEmail()!=null){
            user.setEmail(userDetails.getEmail());
        }
        if(userDetails.getMajor()!=null){
            user.setMajor(userDetails.getMajor());
        }
        if(userDetails.getGrade()!=null){
            user.setGrade(userDetails.getGrade());
        }
        if(userDetails.getPassword()!=null){
            user.setPassword(userDetails.getPassword());
        }


        return userRepository.save(user);
    }

    /**
     * 删除学生
     */
    public void deleteStudent(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("学生不存在，ID: " + id));

        if (!USER_TYPE_STUDENT.equals(user.getUserType())) {
            throw new RuntimeException("ID对应的用户不是学生: " + id);
        }


        userRepository.delete(user);
    }
    // 检查用户ID是否存在
    public boolean existsByUserId(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }

    // 检查邮箱是否存在
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // 保存用户（可以在这里添加密码加密）
    public User saveUser(User user) {
        // 实际生产环境应该加密密码
        // String encodedPassword = passwordEncoder.encode(user.getPassword());
        // user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
    /**
     * 登录验证
     */

    public User findByUserIdAndPassword(String userid, String password) {
        // 实际业务中需加密密码（如BCrypt），此处简化为明文匹配
        return userRepository.findByUserIdAndPassword(userid, password)
                .orElse(null);
    }
    //加密版
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    public User findByUserIdAndPassword(String username, String rawPassword) {
//        Optional<User> userOpt = userRepository.findById(username);
//        if (userOpt.isPresent()) {
//            User user = userOpt.get();
//            // 验证加密密码
//            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
//                return user;
//            }
//        }
//        return null;
//    }
    public Optional<User> findByUserId(String UserId){
        return userRepository.findByUserId(UserId);
    }
}

