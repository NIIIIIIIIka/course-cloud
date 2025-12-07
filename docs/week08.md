

# OpenFeign 配置说明

关于catalog

```java
@FeignClient(
        name = "catalog-service",
        fallbackFactory = CatalogClientFallbackFactory.class
)
public interface CatalogClient {
    @GetMapping("/api/courses/{id}")
    CourseDto getCourse(@PathVariable String id);
}

@Slf4j
@Component
public class CatalogClientFallbackFactory implements FallbackFactory<CatalogClient> {

    @Override
    public CatalogClient create(Throwable cause) {
        log.warn("CatalogService 调用降级，原因：{}", cause.getMessage(), cause);
        return new CatalogClient() {
            @Override
            public CourseDto getCourse(String id) {
                throw new ServiceUnavailableException("课程服务暂时不可用，无法验证课程信息");
            }
        };
    }
}


//调用
            CourseDto courseDto = catalogClient.getCourse(courseId);
```

关于user

```java
@FeignClient(
        name = "user-service",
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {
    // 路径与 FeignStudentController 完全一致：/api/users/students/{id}
    @GetMapping("/api/users/students/{id}")
    StudentDto getStudent(@PathVariable String id);
}


@Slf4j
@Component // 仅工厂类注册为 Bean，而非 UserClient 实现类
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        // 打印降级原因，便于排查
        log.warn("UserService 调用降级，原因：{}", cause.getMessage(), cause);
        // 返回匿名实现类（不会注册为独立 Bean，避免冲突）
        return new UserClient() {
            @Override
            public StudentDto getStudent(String id) {
                // 降级逻辑：抛自定义异常，后续在 Service 中捕获
                throw new ServiceUnavailableException("用户服务暂时不可用，无法验证学生信息");
            }
        };
    }
}

//调用
            StudentDto studentDto = userClient.getStudent(studentId);
```

# 负载均衡测试结果

截图在08.doc中



# 熔断降级测试结果

截图在08.doc中

# OpenFeign vs RestTemplate 对比分析

| 特性     | RestTemplate                                             | OpenFeign                                                    |
| -------- | -------------------------------------------------------- | ------------------------------------------------------------ |
| 核心定位 | 通用 HTTP 客户端工具类（底层封装 HttpClient/OkHttp）     | 声明式 HTTP 客户端（基于 Feign 封装，专注微服务间调用）      |
| 设计理念 | 命令式编程，手动构建请求、处理响应                       | 声明式编程，通过注解定义接口，自动生成实现类                 |
| 底层依赖 | 可适配 JDK HttpURLConnection（默认）、HttpClient、OkHttp | 底层依赖 Feign 核心，可集成 RestTemplate/HttpClient/OkHttp   |
| 生态整合 | Spring Core 原生支持，无额外依赖                         | Spring Cloud 生态核心组件，需引入 `spring-cloud-starter-openfeign` |

