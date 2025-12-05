SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE IF NOT EXISTS `enrollment_db` 
  DEFAULT CHARACTER SET utf8mb4 
  COLLATE utf8mb4_0900_ai_ci;

-- 切换到目标数据库
USE `enrollment_db`;

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
  UNIQUE INDEX `uk_student_course`(`student_id` ASC, `course_id` ASC) USING BTREE
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


SET FOREIGN_KEY_CHECKS = 1;