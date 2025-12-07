package  com.zjgsu.djy.coursecloud.enrollment.client;
import com.zjgsu.djy.coursecloud.enrollment.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "user-service",
        fallbackFactory = UserClientFallbackFactory.class
)
public interface UserClient {
    // 路径与 FeignStudentController 完全一致：/api/users/students/{id}
    @GetMapping("/api/users/students/{id}")
    StudentDto getStudent(@PathVariable String id);
}