package com.zjgsu.djy.coursecloud.user.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元数据路由配置示例
 * 演示如何基于服务实例的元数据进行路由选择
 */
@Configuration
public class MetadataRoutingConfig {

  private static final Logger logger = LoggerFactory.getLogger(MetadataRoutingConfig.class);

  /**
   * 示例控制器，展示如何使用元数据进行服务发现和路由
   */
  @RestController
  @RequestMapping("/api/metadata")
  public static class MetadataRoutingController {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Autowired(required = false)
    private LoadBalancerClient loadBalancerClient;

    /**
     * 获取当前服务实例的元数据
     */
    @GetMapping("/current")
    public Map<String, Object> getCurrentMetadata() {
      Map<String, Object> result = new HashMap<>();
      result.put("serviceName", nacosDiscoveryProperties.getService());
      result.put("namespace", nacosDiscoveryProperties.getNamespace());
      result.put("group", nacosDiscoveryProperties.getGroup());
      result.put("metadata", nacosDiscoveryProperties.getMetadata());

      logger.info("Current service metadata: {}", result);
      return result;
    }

    /**
     * 演示如何根据元数据筛选服务实例
     * 
     * 使用示例：
     * GET /api/metadata/filter?serviceName=catalog-service&version=1.0.0
     * GET /api/metadata/filter?serviceName=catalog-service&zone=zone-A
     */
    @GetMapping("/filter")
    public Map<String, Object> filterByMetadata(
        @RequestParam String serviceName,
        @RequestParam(required = false) String version,
        @RequestParam(required = false) String zone,
        @RequestParam(required = false) String region) {

      Map<String, Object> result = new HashMap<>();
      result.put("serviceName", serviceName);
      result.put("filters", Map.of(
          "version", version != null ? version : "any",
          "zone", zone != null ? zone : "any",
          "region", region != null ? region : "any"));

      // 注意：实际的元数据路由需要集成 Spring Cloud LoadBalancer
      // 这里仅作为示例说明如何使用元数据
      result.put("note", "元数据路由需要配合 Spring Cloud LoadBalancer 或自定义负载均衡策略实现");
      result.put("example", "可以通过 @LoadBalanced RestTemplate 或 Spring Cloud OpenFeign 集成元数据路由");

      logger.info("Filtering service instances by metadata: {}", result);
      return result;
    }

    /**
     * 获取服务的路由规则建议
     */
    @GetMapping("/routing-guide")
    public Map<String, Object> getRoutingGuide() {
      Map<String, Object> guide = new HashMap<>();

      guide.put("description", "基于元数据的服务路由指南");

      guide.put("metadata-fields", Map.of(
          "version", "服务版本号，用于灰度发布和版本路由",
          "zone", "可用区，用于就近访问路由",
          "weight", "权重，用于加权轮询负载均衡",
          "env", "环境标识（dev/test/prod）",
          "region", "地域标识，用于跨地域路由"));

      guide.put("routing-strategies", List.of(
          Map.of("name", "版本路由", "description", "根据 version 元数据路由到指定版本的服务实例"),
          Map.of("name", "就近路由", "description", "根据 zone/region 元数据选择同区域的服务实例"),
          Map.of("name", "加权路由", "description", "根据 weight 元数据按权重分配流量"),
          Map.of("name", "环境隔离", "description", "根据 env 元数据确保只调用同环境的服务")));

      guide.put("implementation-options", List.of(
          "使用 Spring Cloud LoadBalancer 自定义负载均衡策略",
          "使用 Nacos 权重配置实现流量分配",
          "使用 Spring Cloud Gateway 实现网关层路由",
          "使用自定义 RestTemplate 拦截器实现元数据过滤"));

      return guide;
    }
  }
}
