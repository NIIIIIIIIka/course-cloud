-- 创建数据库
CREATE DATABASE IF NOT EXISTS user_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE user_db;


CREATE TABLE `user` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统生成UUID（唯一标识）',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号（学生为学号，老师为工号，全局唯一）',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱（必填，格式校验）',

  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户类型：student / teacher',

  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业（仅学生需要）',
  `grade` int NULL DEFAULT NULL COMMENT '入学年份（仅学生需要）',

  `created_at` datetime NOT NULL COMMENT '创建时间戳（系统生成）',

  PRIMARY KEY (`id`) USING BTREE,

  UNIQUE INDEX `UK_user_userId` (`user_id`) USING BTREE,
  UNIQUE INDEX `UK_user_email` (`email`) USING BTREE,

  CONSTRAINT `chk_user_email_format` CHECK (`email` LIKE '%_@__%.__%'),
  CONSTRAINT `chk_user_type` CHECK (`user_type` IN ('student', 'teacher'))
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '系统用户表（学生与老师统一存储）'
  ROW_FORMAT = Dynamic;



INSERT INTO user (id, user_id, name, email, user_type, major, grade, created_at)
VALUES
('8f1e2a78-4c21-11ef-b7a9-0242ac120002', '20230001', '张三', 'zhangsan@example.com', 'student', '计算机科学与技术', 2023, NOW()),
('8f1e2c90-4c21-11ef-b7a9-0242ac120002', '20230002', '李四', 'lisi@example.com', 'student', '软件工程', 2023, NOW()),
('8f1e2dc8-4c21-11ef-b7a9-0242ac120002', '20230003', '王五', 'wangwu@example.com', 'student', '信息管理与信息系统', 2023, NOW());


INSERT INTO user (id, user_id, name, email, user_type, major, grade, created_at)
VALUES
('9a3f2b11-4c21-11ef-b7a9-0242ac120002', 'T1001', '赵老师', 'zhaols@example.com', 'teacher', NULL, NULL, NOW()),
('9a3f2c44-4c21-11ef-b7a9-0242ac120002', 'T1002', '钱老师', 'qianls@example.com', 'teacher', NULL, NULL, NOW());
