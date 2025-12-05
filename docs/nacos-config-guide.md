# Nacos 配置中心使用说明

本目录包含需要在 Nacos 配置中心创建的配置文件示例。

## 配置文件列表

### 1. 服务专属配置

#### user-service.yml
- **Data ID**: `user-service.yml`
- **Group**: `COURSEHUB_GROUP`
- **Namespace**: `dev`
- **说明**: 用户服务的专属配置

#### catalog-service.yml
- **Data ID**: `catalog-service.yml`
- **Group**: `COURSEHUB_GROUP`
- **Namespace**: `dev`
- **说明**: 课程目录服务的专属配置

#### enrollment-service.yml
- **Data ID**: `enrollment-service.yml`
- **Group**: `COURSEHUB_GROUP`
- **Namespace**: `dev`
- **说明**: 选课服务的专属配置

### 2. 共享配置

#### common-database.yml
- **Data ID**: `common-database.yml`
- **Group**: `COMMON_GROUP`
- **Namespace**: `dev`
- **说明**: 数据库连接池等通用配置

#### common-logging.yml
- **Data ID**: `common-logging.yml`
- **Group**: `COMMON_GROUP`
- **Namespace**: `dev`
- **说明**: 日志级别等通用配置

## 如何使用

### 1. 登录 Nacos 控制台
访问: http://localhost:8848/nacos
默认账号/密码: nacos/nacos

### 2. 创建命名空间（如果还没有）
- 进入"命名空间"页面
- 创建命名空间 ID: `dev`，名称: `开发环境`
- 创建命名空间 ID: `test`，名称: `测试环境`

### 3. 创建配置
- 进入"配置管理" -> "配置列表"
- 选择对应的命名空间（dev）
- 点击 "+" 创建配置
- 填写 Data ID、Group、配置格式（YAML）
- 粘贴对应的配置内容
- 点击"发布"

### 4. 验证配置
启动服务后，访问配置查询接口：
```
GET http://localhost:8081/api/config/current
```

### 5. 测试配置刷新
- 在 Nacos 控制台修改配置
- 无需重启服务
- 再次调用接口查看配置已更新

## 配置优先级

1. bootstrap.yml（最高优先级，用于配置 Nacos 连接）
2. Nacos 配置中心的配置
3. application.yml（本地配置，作为默认值）

## 注意事项

1. **敏感信息**: 数据库密码等敏感信息建议使用 Nacos 加密配置
2. **环境隔离**: 使用不同的 namespace 隔离 dev/test/prod 环境
3. **配置备份**: 定期备份 Nacos 配置
4. **配置监听**: 使用 @RefreshScope 注解实现配置动态刷新
