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
CREATE DATABASE IF NOT EXISTS `catalog_db` 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_0900_ai_ci;

-- 切换到目标数据库
USE `catalog_db`;
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
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES ('403c1a72-2d8c-ed5c-eea2-78155900496e', 'CS321', '职业规划', 84, 35, '2025-10-23 23:46:37', 'T061', 'Vincent Walker', 'vincentwalk@icloud.com', '星期二', '15:00', '18:00', 34);
INSERT INTO `course` VALUES ('5b8e4836-f019-4abd-e4b9-2e22da2c4c08', 'CS033', '计算机网络', 56, 38, '2025-10-04 10:12:19', 'T152', 'Heather Bell', 'bhe1@mail.com', '星期一', '11:00', '18:00', 12);
INSERT INTO `course` VALUES ('7029b9aa-b9be-5e1d-bdbe-1ae1799dc9b9', 'CS100', '软件工程', 45, 23, '2025-10-06 22:05:09', 'T850', 'Vincent Payne', 'vincentp@outlook.com', '星期一', '9:00', '10:00', 39);
INSERT INTO `course` VALUES ('78f8c0fc-688d-e383-3259-85566ddc80da', 'CS323', 'C语言', 74, 2, '2025-08-22 12:34:05', 'T641', '傅震南', 'zhennanfu716@icloud.com', '星期一', '15:00', '17:00', 23);
INSERT INTO `course` VALUES ('8bf4264e-8a3b-42ca-96ed-9bd6ffe0386e', 'CS102', '算法设计', 60, 0, '2025-11-02 18:05:11', 'T002', '江照意', 'jiang@example.edu.cn', '星期四', '08:00', '10:00', 50);
INSERT INTO `course` VALUES ('9b82339b-ffcc-1bdf-be72-279a656ec272', 'CS010', '操作系统', 82, 15, '2025-10-13 20:11:09', 'T601', 'Danielle Medina', 'daniellemedina4@gmail.com', '星期三', '09:00', '10:00', 21);
INSERT INTO `course` VALUES ('9e2662b9-b92b-7e52-2f28-83c754a0bff2', 'CS020', '数据库原理', 68, 6, '2025-09-09 08:18:20', 'T824', '丁嘉伦', 'jialudin1018@gmail.com', '星期二', '09:00', '10:00', 42);
INSERT INTO `course` VALUES ('abbdb3c8-23a6-0498-a330-984ed28af6fb', 'CS333', '桌面应用', 93, 18, '2025-08-14 18:26:52', 'T193', '杜震南', 'zhennandu@icloud.com', '星期二', '16:00', '15:00', 14);
INSERT INTO `course` VALUES ('b58a96cd-949f-e038-9a0c-b2fd3fc65035', 'CS201', '微服务设计', 42, 29, '2025-09-20 00:37:50', 'T316', '任震南', 'renz10@hotmail.com', '星期五', '15:00', '16:00', 39);
INSERT INTO `course` VALUES ('dc19a00f-0d7d-b62f-0c78-5b95f451f0ae', 'CS132', '机器学习', 47, 25, '2025-09-10 00:01:17', 'T585', 'Cheng Tin Wing', 'tinwing1@hotmail.com', '星期三', '14:00', '15:00', 29);
INSERT INTO `course` VALUES ('e215db24-1ae6-6836-9feb-f9ba719e2f46', 'CS331', '计算机系统', 84, 23, '2025-10-05 04:04:12', 'T333', '程秀英', 'cxiuying@icloud.com', '星期三', '14:00', '15:00', 50);



SET FOREIGN_KEY_CHECKS = 1;