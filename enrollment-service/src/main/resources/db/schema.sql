/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80037 (8.0.37)
 Source Host           : localhost:3306
 Source Schema         : course

 Target Server Type    : MySQL
 Target Server Version : 80037 (8.0.37)
 File Encoding         : 65001

 Date: 02/11/2025 19:22:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程ID（系统生成UUID）',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程编码（如CS101）',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程名称',
  `capacity` int UNSIGNED NOT NULL COMMENT '课程容量',
  `enrolled` int NOT NULL DEFAULT 0 COMMENT '已选人数（默认0）',
  `created_at` datetime NOT NULL COMMENT '创建时间（系统生成）',
  `instructor_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `instructor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '讲师姓名',
  `instructor_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '讲师邮箱',
  `schedule_day_of_week` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `schedule_start_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `schedule_end_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expected_attendance` int NULL DEFAULT NULL COMMENT '预计出勤人数',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_course_code`(`code` ASC) USING BTREE COMMENT '课程编码唯一约束',
  UNIQUE INDEX `UK_instructor_email`(`instructor_email` ASC) USING BTREE,
  UNIQUE INDEX `UKi60mruj0y7a7vs99dqpiye7en`(`code` ASC) USING BTREE,
  CONSTRAINT `chk_course_enrolled_capacity` CHECK (`enrolled` <= `capacity`),
  CONSTRAINT `chk_course_capacity_positive` CHECK (`capacity` > 0),
  CONSTRAINT `chk_course_enrolled_non_negative` CHECK (`enrolled` >= 0),
  CONSTRAINT `chk_teacher_email_format` CHECK (`instructor_email` like _utf8mb4'%_@__%.__%')
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表（含嵌入式讲师、排课信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统生成UUID（唯一标识）',
  `student_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号（全局唯一）',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名（必填）',
  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业（必填）',
  `grade` int NULL DEFAULT NULL COMMENT '入学年份（必填）',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱（必填，格式校验）',
  `created_at` datetime NOT NULL COMMENT '创建时间戳（系统生成）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_student_id`(`id` ASC) USING BTREE COMMENT '主键唯一约束',
  UNIQUE INDEX `UK_student_email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `UK_student_studentId`(`student_id` ASC) USING BTREE,
  CONSTRAINT `chk_student_email_format` CHECK (`email` like _utf8mb4'%_@__%.__%')
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for enrollment
-- ----------------------------
DROP TABLE IF EXISTS `enrollment`;
CREATE TABLE `enrollment`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联课程ID（对应course表id）',
  `student_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联学生ID（对应student表id）',
  `enroll_time` datetime(6) NOT NULL,
  `status` enum('ACTIVE','COMPLETED','DROPPED','EXPIRED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_enrollment_id`(`id` ASC) USING BTREE COMMENT '选课记录ID唯一约束',
  UNIQUE INDEX `UK_course_student`(`course_id` ASC, `student_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_student_course`(`student_id` ASC, `course_id` ASC) USING BTREE,
  CONSTRAINT `fk_enrollment_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_enrollment_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '选课记录表' ROW_FORMAT = Dynamic;

