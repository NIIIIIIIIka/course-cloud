@echo off
chcp 65001 >nul 2>&1
setlocal enabledelayedexpansion

:: 设置控制台支持UTF-8
if defined _started_from_powershell (
    powershell -Command "[Console]::OutputEncoding = [System.Text.Encoding]::UTF8"
)

:: 基础URL（请根据实际部署地址修改）
set BASE_URL=http://localhost:8081/api

echo ==============================================
echo 开始测试学生相关接口
echo ==============================================

:: 1. 创建学生
echo [1/5] 测试创建学生...
curl -X POST "%BASE_URL%/students" ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":\"20230004\",\"name\":\"测试人员\",\"email\":\"test@example.com\",\"userType\":\"student\",\"major\":\"计算机科学与技术\",\"grade\":2023}"
echo.
echo --------------------------------------------------

:: 2. 获取所有学生信息
echo [2/5] 测试获取所有学生...
curl -X GET "%BASE_URL%/students"
echo.
echo --------------------------------------------------

:: 3. 通过ID获取学生信息（假设创建成功后返回的ID为1，实际测试时需替换为真实ID）
set STUDENT_ID=8f1e2a78-4c21-11ef-b7a9-0242ac120002
echo [3/5] 测试获取ID为%STUDENT_ID%的学生...
curl -X GET "%BASE_URL%/students/%STUDENT_ID%"
echo.
echo --------------------------------------------------

:: 4. 更新学生信息
echo [4/5] 测试更新学生信息...
curl -X PUT "%BASE_URL%/students/%STUDENT_ID%" ^
  -H "Content-Type: application/json" ^
  -d "{\"userId\":\"20230004\",\"name\":\"更新后的姓名\",\"email\":\"updated@example.com\",\"major\":\"更新后的专业\",\"grade\":2025}"
echo.
echo --------------------------------------------------

:: 5. 通过学号获取学生信息
echo [5/5] 测试通过学号获取学生...
curl -X GET "%BASE_URL%/students/studentId/20230004"
echo.
echo --------------------------------------------------


echo ==============================================
echo 开始测试课程相关接口
echo ==============================================
set BASE_URL=http://localhost:8082/api
:: 1. 创建课程
echo [1/6] 测试创建课程...
curl -X POST "%BASE_URL%/courses" ^
  -H "Content-Type: application/json" ^
  -d "{\"code\":\"CS101\",\"title\":\"计算机科学导论\",\"capacity\":50,\"enrolled\":0,\"instructorId\":\"T001\",\"instructorName\":\"张三\",\"instructorEmail\":\"zhangsan@example.com\",\"scheduleDayOfWeek\":\"星期一\",\"scheduleStartTime\":\"09:00\",\"scheduleEndTime\":\"11:00\",\"expectedAttendance\":45}"
echo.
echo --------------------------------------------------

:: 2. 获取所有课程信息
echo [2/6] 测试获取所有课程...
curl -X GET "%BASE_URL%/courses"
echo.
echo --------------------------------------------------

:: 3. 通过ID获取课程信息（假设创建成功后返回的ID为1，实际测试时需替换为真实ID）
set COURSE_ID=403c1a72-2d8c-ed5c-eea2-78155900496e
echo [3/6] 测试获取ID为%COURSE_ID%的课程...
curl -X GET "%BASE_URL%/courses/%COURSE_ID%"
echo.
echo --------------------------------------------------

:: 4. 更新课程信息
echo [4/6] 测试更新课程信息...
curl -X PUT "%BASE_URL%/courses/%COURSE_ID%" ^
  -H "Content-Type: application/json" ^
  -d "{\"code\":\"CS101\",\"title\":\"更新后课程名字\",\"capacity\":50,\"enrolled\":0,\"instructorId\":\"T001\",\"instructorName\":\"张三\",\"instructorEmail\":\"zhangsan@example.com\",\"scheduleDayOfWeek\":\"星期一\",\"scheduleStartTime\":\"09:00\",\"scheduleEndTime\":\"11:00\",\"expectedAttendance\":45}"
echo.
echo --------------------------------------------------

:: 5. 通过课程编码获取课程信息
echo [5/6] 测试通过课程编码获取课程...
curl -X GET "%BASE_URL%/courses/code/CS101"
echo.
echo --------------------------------------------------

:: 6. 删除课程（测试完成后清理）
echo [6/6] 测试删除课程...
curl -X DELETE "%BASE_URL%/courses/%COURSE_ID%"
echo.
echo --------------------------------------------------


echo ==============================================
echo 开始测试选课相关接口
echo ==============================================
set BASE_URL=http://localhost:8083/api
:: 1. 创建选课记录
echo [1/5] 测试学生选课...
curl -X POST "%BASE_URL%/enrollments" ^
  -H "Content-Type: application/json" ^
  -d "{\"courseId\":\"%COURSE_ID%\",\"studentId\":\"%STUDENT_ID%\"}"
echo.
echo --------------------------------------------------

:: 2. 获取所有选课记录
echo [2/5] 测试获取所有选课记录...
curl -X GET "%BASE_URL%/enrollments"
echo.
echo --------------------------------------------------

:: 3. 按课程查询选课
echo [3/5] 测试查询课程%COURSE_ID%的选课记录...
curl -X GET "%BASE_URL%/enrollments/course/%COURSE_ID%"
echo.
echo --------------------------------------------------

:: 4. 按学生查询选课
echo [4/5] 测试查询学生%STUDENT_ID%的选课记录...
curl -X GET "%BASE_URL%/enrollments/student/%STUDENT_ID%"
echo.
echo --------------------------------------------------

:: 5. 删除选课记录（假设创建成功后返回的ID为1，实际测试时需替换为真实ID）
set ENROLLMENT_ID=0085df31-ba0c-9b88-cabc-915094599177
echo [5/5] 测试删除选课记录...
curl -X DELETE "%BASE_URL%/enrollments/%ENROLLMENT_ID%"
echo.
echo --------------------------------------------------


echo ==============================================
echo 开始清理测试数据（删除学生）
echo ==============================================
curl -X DELETE "%BASE_URL%/students/%STUDENT_ID%"
echo.

echo ==============================================
echo 所有接口测试完成
echo ==============================================
endlocal