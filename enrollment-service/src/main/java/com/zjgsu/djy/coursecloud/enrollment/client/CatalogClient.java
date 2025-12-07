package  com.zjgsu.djy.coursecloud.enrollment.client;
import com.zjgsu.djy.coursecloud.enrollment.dto.CourseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "catalog-service",
        fallbackFactory = CatalogClientFallbackFactory.class
)
public interface CatalogClient {
    @GetMapping("/api/courses/{id}")
    CourseDto getCourse(@PathVariable String id);
}