package  com.zjgsu.djy.coursecloud.enrollment.client;
import com.zjgsu.djy.coursecloud.enrollment.dto.StudentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

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