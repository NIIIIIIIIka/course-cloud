package com.zjgsu.djy.coursecloud.user.controller;

import com.zjgsu.djy.coursecloud.user.model.User;
import com.zjgsu.djy.coursecloud.user.service.UserService;
import com.zjgsu.djy.coursecloud.user.util.JwtUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 认证控制器（登录/注册/生成Token）
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录请求体
     */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "用户名不能为空")
        private String username; // 学号/管理员账号

        @NotBlank(message = "密码不能为空")
        private String password;
    }

    /**
     * 注册请求体
     */
    @Data
    public static class RegisterRequest {
        @NotBlank(message = "用户编号不能为空")
        private String userId; // 学号/工号

        @NotBlank(message = "密码不能为空")
        private String password;
//
//        @NotBlank(message = "确认密码不能为空")
        private String confirmPassword;
//
//        @NotBlank(message = "姓名不能为空")
        private String name;
//
//        @NotBlank(message = "邮箱不能为空")
//        @Email(message = "邮箱格式不正确")
        private String email;
//
//        @NotBlank(message = "用户类型不能为空")
        private String userType; // student/teacher

        private String major; // 专业（学生需要）
        private Integer grade; // 年级（学生需要）
    }

    /**
     * 登录接口（生成JWT Token）
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        System.out.println("登录请求: username=" + request.getUsername());

        // 1. 验证用户名和密码
        User user = userService.findByUserIdAndPassword(request.getUsername(), request.getPassword());
        if (user == null) {
            System.out.println("用户不存在或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "用户名或密码错误"));
        }

        System.out.println("用户验证成功: " + user.getUserId());

        // 2. 生成JWT Token（使用user_type作为角色）
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUserId(),
                user.getUserType() // 使用user_type作为角色
        );

        // 3. 返回Token和用户信息
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "userId", user.getUserId(),
                        "role", user.getUserType(), // 返回user_type作为角色
                        "name", user.getName(),
                        "email", user.getEmail()
                )
        ));
    }

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("注册请求: userId=" + request.getUserId() + ", email=" + request.getEmail());

        // 1. 验证两次密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "两次输入的密码不一致"));
        }

        // 2. 检查用户是否已存在
        if (userService.existsByUserId(request.getUserId())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "用户编号已存在"));
        }

        // 3. 检查邮箱是否已存在
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "邮箱已被注册"));
        }

        // 4. 创建用户
        User user = new User();
        user.setUserId(request.getUserId());
        user.setPassword(request.getPassword()); // 注意：实际应该加密存储
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserType(request.getUserType());
        user.setMajor(request.getMajor());
        user.setGrade(request.getGrade());

        // 5. 设置角色（根据user_type映射）
        String role="student";
        user.setUserType(role); // 如果有role字段的话

        // 6. 保存用户
        try {
            User savedUser = userService.saveUser(user);
            System.out.println("用户注册成功: " + savedUser.getUserId());

            // 7. 生成JWT Token并返回
            String token = jwtUtil.generateToken(
                    savedUser.getId(),
                    savedUser.getUserId(),
                    savedUser.getUserType()
            );

            return ResponseEntity.ok(Map.of(
                    "message", "注册成功",
                    "token", token,
                    "user", Map.of(
                            "id", savedUser.getId(),
                            "userId", savedUser.getUserId(),
                            "name", savedUser.getName(),
                            "email", savedUser.getEmail(),
                            "role", savedUser.getUserType()
                    )
            ));

        } catch (Exception e) {
            System.out.println("注册失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "注册失败，请稍后重试"));
        }
    }
}