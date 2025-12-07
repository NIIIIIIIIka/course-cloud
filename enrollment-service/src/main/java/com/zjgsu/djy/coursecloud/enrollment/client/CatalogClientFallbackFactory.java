package  com.zjgsu.djy.coursecloud.enrollment.client;
import com.zjgsu.djy.coursecloud.enrollment.dto.CourseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

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