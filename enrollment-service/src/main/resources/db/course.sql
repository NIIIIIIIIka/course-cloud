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

-- ----------------------------
-- Records of enrollment
-- ----------------------------
INSERT INTO `enrollment` VALUES ('0085df31-ba0c-9b88-cabc-915094599177', '5b8e4836-f019-4abd-e4b9-2e22da2c4c08', '9b066ac4-5529-986f-8129-c4386b230b0c', '2025-11-01 17:50:34.000000', 'COMPLETED');
INSERT INTO `enrollment` VALUES ('10c7ddd2-2742-93b7-4b10-8186246601c7', '78f8c0fc-688d-e383-3259-85566ddc80da', '69610491-0dca-4fb1-8aa3-4f6823477a53', '2025-11-01 09:14:04.000000', 'EXPIRED');
INSERT INTO `enrollment` VALUES ('38d3bb2a-28c7-996a-9dda-740f55ae420e', '8bf4264e-8a3b-42ca-96ed-9bd6ffe0386e', '93b6699a-14a1-4ede-92ff-5f0b1ff5cd45', '2025-10-31 22:28:24.000000', 'EXPIRED');
INSERT INTO `enrollment` VALUES ('5892e88c-f033-331d-0971-d18e3fc932b5', '7029b9aa-b9be-5e1d-bdbe-1ae1799dc9b9', 'a2fe337b-4a26-9605-2653-a6322a35b988', '2025-10-31 02:59:52.000000', 'EXPIRED');
INSERT INTO `enrollment` VALUES ('630be9fe-5ddc-d213-6bd7-b0c9ae6d5970', '7029b9aa-b9be-5e1d-bdbe-1ae1799dc9b9', '330306c4-6a70-434e-9107-9ab3f97fff13', '2025-10-31 06:05:51.000000', 'ACTIVE');
INSERT INTO `enrollment` VALUES ('6b1b19ca-9578-a4d6-fdb6-81044942a6fc', '9e2662b9-b92b-7e52-2f28-83c754a0bff2', '241f819e-71fe-0692-e873-f42bb47ecfc5', '2025-11-02 22:13:31.000000', 'ACTIVE');
INSERT INTO `enrollment` VALUES ('8df72b17-24a8-16f2-f575-c48ffb2337b8', '7029b9aa-b9be-5e1d-bdbe-1ae1799dc9b9', 'c4f61abb-a509-403d-8d8e-804d14be0d54', '2025-10-30 01:00:34.000000', 'DROPPED');
INSERT INTO `enrollment` VALUES ('a1f05c68-7985-45dd-f1bd-a6a177e66f7f', '8bf4264e-8a3b-42ca-96ed-9bd6ffe0386e', '9f7dccfc-1638-7086-ca6a-42658b728435', '2025-10-31 16:25:41.000000', 'COMPLETED');
INSERT INTO `enrollment` VALUES ('a74c7443-6225-f842-4532-f18272806d4d', '8bf4264e-8a3b-42ca-96ed-9bd6ffe0386e', '69610491-0dca-4fb1-8aa3-4f6823477a53', '2025-10-30 06:50:32.000000', 'ACTIVE');
INSERT INTO `enrollment` VALUES ('f546b2f7-218a-cb69-e7d9-3d553ba8a9f7', '9b82339b-ffcc-1bdf-be72-279a656ec272', 'a2fe337b-4a26-9605-2653-a6322a35b988', '2025-10-31 01:19:27.000000', 'DROPPED');

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
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('0370fdfd-00a1-3c69-2a02-3a3fa922240d', 'S031', 'Esther Jordan', '计算机科学与技术', 2021, 'jordae92@outlook.com', '2023-04-03 05:18:09');
INSERT INTO `student` VALUES ('06d64085-0b7b-4759-a203-dc3653ba9d9a', 'S001', '张三', '计算机科学与技术', 2024, 'zh2angllli@example.edu.cn', '2025-10-27 23:56:58');
INSERT INTO `student` VALUES ('1293c21e-9947-49f1-a006-8b6f4628d71e', 'S033', '张y', '计算机科学与技术', 2024, 'zhangllli@example.ed2u.cn', '2025-11-02 18:07:20');
INSERT INTO `student` VALUES ('20bbfd67-62d5-4f8f-9cd6-5857505b525f', 'S032', 'Cho Wai Man', '信息安全', 2022, 'wmcho@icloud.com', '2024-03-19 15:58:19');
INSERT INTO `student` VALUES ('241f819e-71fe-0692-e873-f42bb47ecfc5', 'S036', 'Julia Gibson', '计算机科学与技术', 2024, 'gibsonjulia@gmail.com', '2024-11-08 17:49:56');
INSERT INTO `student` VALUES ('330306c4-6a70-434e-9107-9ab3f97fff13', 'S038', '张y', '计算机科学与技术', 2024, 'zhang2llli@example.edu.cn', '2025-10-27 23:58:52');
INSERT INTO `student` VALUES ('3e6af2a5-f6d9-aa6d-58ce-9c24a1ef4720', 'S039', 'Katherine Adams', '信息安全', 2021, 'adamkatherine@yahoo.com', '2024-07-30 10:13:28');
INSERT INTO `student` VALUES ('69610491-0dca-4fb1-8aa3-4f6823477a53', 'S003', '王五', '计算机科学与技术', 2024, 'wwi@example.edu.cn', '2025-10-27 23:56:58');
INSERT INTO `student` VALUES ('831b264c-9f9d-fa0e-a303-e7e84bcc05aa', 'S004', 'Vincent Mendez', '信息安全', 2023, 'mendez408@hotmail.com', '2023-04-03 20:32:44');
INSERT INTO `student` VALUES ('8a5e896a-c1b0-4ea9-3966-91287d70dd76', 'S006', 'Mak Ling Ling', '计算机科学与技术', 2023, 'makll9@gmail.com', '2022-02-22 21:57:50');
INSERT INTO `student` VALUES ('93b6699a-14a1-4ede-92ff-5f0b1ff5cd45', 'S020', 'djy', '计算机科学与技术', 2024, 'zhangllli@example.edu.cn', '2025-11-02 18:07:34');
INSERT INTO `student` VALUES ('9b066ac4-5529-986f-8129-c4386b230b0c', 'S021', 'Bonnie Ruiz', '计算机科学与技术', 2024, 'ruizbo75@hotmail.com', '2023-11-18 01:43:01');
INSERT INTO `student` VALUES ('9f7dccfc-1638-7086-ca6a-42658b728435', 'S025', 'Crystal Walker', '计算机科学与技术', 2024, 'walkecr216@icloud.com', '2021-10-14 08:25:07');
INSERT INTO `student` VALUES ('a2fe337b-4a26-9605-2653-a6322a35b988', 'S029', 'Alice Kelly', '信息安全', 2025, 'kellyalice@gmail.com', '2025-04-17 08:36:03');
INSERT INTO `student` VALUES ('a5c1a879-e9ec-fc29-427a-1bfc78800d09', 'S129', 'Carmen Rogers', '软件工程', 2024, 'rogersc@gmail.com', '2024-04-25 08:15:02');
INSERT INTO `student` VALUES ('c4f61abb-a509-403d-8d8e-804d14be0d54', 'S002', '李四', '计算机科学与技术', 2024, 'lisi@example.edu.cn', '2025-10-27 23:56:58');

SET FOREIGN_KEY_CHECKS = 1;
