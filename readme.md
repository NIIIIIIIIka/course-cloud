# 项目简介
项目名称：course-cloud

基于版本：1.1.0

微服务架构说明
# 架构和运行步骤

## 架构图

            ┌─────────────────────┐
            │    管理员/用户        │
            │   (外部访问)         │
            └─────────┬───────────┘
                      │
          ┌───────────┼───────────┐
          │           │           │
    ┌─────▼────┐ ┌────▼────┐ ┌────▼────┐
    │ 用户服务  │ │  课程服务 │ │ 选课服务 │
    │ (8081)   │ │ (8082)  │ │ (8083)  │
    └─────┬────┘ └────┬────┘ └────┬────┘
          │           │           │
          │           │      ┌────┴────┐
          │           │      │ HTTP调用 │
          │           │      └────┬────┘
          │           │           │
    ┌─────▼────┐ ┌────▼────┐ ┌────▼────┐
    │用户数据库  │ │课程数据库 │ │选课数据库│
    │ (3307)   │ │ (3308)  │ │ (3309)  │
    └──────────┘ └─────────┘ └─────────┘

关键特性：
• 每个服务独立部署
• 每个服务有自己的数据库
• 选课服务需要调用其他两个服务
• 通过Docker Compose统一管理

## 架构关键特点：

### 1. **三层架构**

- **访问层**：外部HTTP访问
- **服务层**：三个独立的微服务
- **数据层**：三个独立的数据库

### 2. **微服务特性**

- **独立部署**：每个服务有自己的端口和数据库
- **服务间通信**：选课服务通过HTTP调用用户服务和课程服务
- **松耦合**：每个服务有自己的数据存储

### 3. **数据库隔离**

text

```
┌─────────────┬──────────────┬──────────────┐
│   服务名称   │   数据库名    │   端口映射    │
├─────────────┼──────────────┼──────────────┤
│ user-service│ user_db      │ 3307 → 3306  │
│ catalog-svc │ catalog_db   │ 3308 → 3306  │
│ enrollment  │ enrollment_db│ 3309 → 3306  │
└─────────────┴──────────────┴──────────────┘
```



### 4. **数据流**

```
1. 用户访问 → 服务层 → 数据库层
2. 选课服务 → (验证用户信息) → 用户服务
3. 选课服务 → (验证课程信息) → 课程服务
4. 选课服务 → 存储选课记录 → 选课数据库
```



### 5. **容器化特性**

- **独立容器**：每个服务运行在独立容器中
- **网络隔离**：所有容器通过 `course-network` 通信
- **健康检查**：数据库有健康检查机制
- **数据持久化**：使用Docker卷持久化数据
- **重启策略**：`restart: unless-stopped`


# 接口文档

# user

## GET 获取所有学生信息

GET /api/students

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## POST 创建学生

POST /api/students

> Body 请求参数

```json
{
  "userId": "20230004",
  "name": "测试人员",
  "email": "test@example.com",
  "userType": "student",
  "major": "计算机科学与技术",
  "grade": 2023
}
```

### 请求参数

| 名称       | 位置 | 类型    | 必选 | 说明 |
| ---------- | ---- | ------- | ---- | ---- |
| body       | body | object  | 否   | none |
| » userId   | body | string  | 是   | none |
| » name     | body | string  | 是   | none |
| » email    | body | string  | 是   | none |
| » userType | body | string  | 是   | none |
| » major    | body | string  | 是   | none |
| » grade    | body | integer | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 通过id获取学生信息

GET /api/students/{id}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| id   | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## PUT 更新学生信息

PUT /api/students/{id}

> Body 请求参数

```json
{
  "userId": "20230004",
  "name": "更新后的姓名",
  "email": "updated@example.com",
  "major": "更新后的专业",
  "grade": 2025
}
```

### 请求参数

| 名称     | 位置 | 类型    | 必选 | 说明 |
| -------- | ---- | ------- | ---- | ---- |
| id       | path | string  | 是   | none |
| body     | body | object  | 否   | none |
| » userId | body | string  | 是   | none |
| » name   | body | string  | 是   | none |
| » email  | body | string  | 是   | none |
| » major  | body | string  | 是   | none |
| » grade  | body | integer | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## DELETE 删除学生

DELETE /api/students/{id}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| id   | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 通过学号获取学生信息

GET /api/students/studentId/{studentId}

### 请求参数

| 名称      | 位置 | 类型   | 必选 | 说明 |
| --------- | ---- | ------ | ---- | ---- |
| studentId | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

# catalog

## GET 获取所有课程信息

GET /api/courses

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## POST 创建课程

POST /api/courses

> Body 请求参数

```json
{
  "code": "CS101",
  "title": "计算机科学导论",
  "capacity": 50,
  "enrolled": 0,
  "instructorId": "T001",
  "instructorName": "张三",
  "instructorEmail": "zhangsan@example.com",
  "scheduleDayOfWeek": "星期一",
  "scheduleStartTime": "09:00",
  "scheduleEndTime": "11:00",
  "expectedAttendance": 45
}
```

### 请求参数

| 名称                 | 位置 | 类型    | 必选 | 说明 |
| -------------------- | ---- | ------- | ---- | ---- |
| body                 | body | object  | 否   | none |
| » code               | body | string  | 是   | none |
| » title              | body | string  | 是   | none |
| » capacity           | body | integer | 是   | none |
| » enrolled           | body | integer | 是   | none |
| » instructorId       | body | string  | 是   | none |
| » instructorName     | body | string  | 是   | none |
| » instructorEmail    | body | string  | 是   | none |
| » scheduleDayOfWeek  | body | string  | 是   | none |
| » scheduleStartTime  | body | string  | 是   | none |
| » scheduleEndTime    | body | string  | 是   | none |
| » expectedAttendance | body | integer | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 通过id获取课程信息

GET /api/courses/{id}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| id   | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## PUT 更新课程

PUT /api/courses/{id}

> Body 请求参数

```json
{
  "code": "CS101",
  "title": "更新后课程名字",
  "capacity": 50,
  "enrolled": 0,
  "instructorId": "T001",
  "instructorName": "张三",
  "instructorEmail": "zhangsan@example.com",
  "scheduleDayOfWeek": "星期一",
  "scheduleStartTime": "09:00",
  "scheduleEndTime": "11:00",
  "expectedAttendance": 45
}
```

### 请求参数

| 名称                 | 位置 | 类型    | 必选 | 说明 |
| -------------------- | ---- | ------- | ---- | ---- |
| id                   | path | string  | 是   | none |
| body                 | body | object  | 否   | none |
| » code               | body | string  | 是   | none |
| » title              | body | string  | 是   | none |
| » capacity           | body | integer | 是   | none |
| » enrolled           | body | integer | 是   | none |
| » instructorId       | body | string  | 是   | none |
| » instructorName     | body | string  | 是   | none |
| » instructorEmail    | body | string  | 是   | none |
| » scheduleDayOfWeek  | body | string  | 是   | none |
| » scheduleStartTime  | body | string  | 是   | none |
| » scheduleEndTime    | body | string  | 是   | none |
| » expectedAttendance | body | integer | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## DELETE 删除课程

DELETE /api/courses/{id}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| id   | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 通过课程编码获取课程信息

GET /api/courses/code/{code}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| code | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

# enrollment

## GET 获取所有选课记录

GET /api/enrollments

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## POST 学生选课

POST /api/enrollments

> Body 请求参数

```json
{
  "courseId": "5b8e4836-f019-4abd-e4b9-2e22da2c4c08",
  "studentId": "9b066ac4-5529-986f-8129-c4386b230b0c"
}
```

### 请求参数

| 名称        | 位置 | 类型   | 必选 | 说明 |
| ----------- | ---- | ------ | ---- | ---- |
| body        | body | object | 否   | none |
| » courseId  | body | string | 是   | none |
| » studentId | body | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 按课程查询选课

GET /api/enrollments/course/{courseId}

### 请求参数

| 名称     | 位置 | 类型   | 必选 | 说明 |
| -------- | ---- | ------ | ---- | ---- |
| courseId | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## GET 按学生查询选课

GET /api/enrollments/student/{studentId}

### 请求参数

| 名称      | 位置 | 类型   | 必选 | 说明 |
| --------- | ---- | ------ | ---- | ---- |
| studentId | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 返回数据结构

## DELETE 学生退课

DELETE /api/enrollments/{id}

### 请求参数

| 名称 | 位置 | 类型   | 必选 | 说明 |
| ---- | ---- | ------ | ---- | ---- |
| id   | path | string | 是   | none |

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

| 状态码 | 状态码含义                                              | 说明 | 数据模型 |
| ------ | ------------------------------------------------------- | ---- | -------- |
| 200    | [OK](https://tools.ietf.org/html/rfc7231#section-6.3.1) | none | Inline   |

### 

# 测试说明
运行test.bat
