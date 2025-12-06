package com.zjgsu.djy.coursecloud.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Nacos 配置管理示例
 * 
 * 使用 @RefreshScope 注解实现配置动态刷新
 * 当 Nacos 中的配置更新时，该类的配置会自动刷新
 */
@RefreshScope
@RestController
@RequestMapping("/api/config")
public class NacosConfigExample {

  private static final Logger logger = LoggerFactory.getLogger(NacosConfigExample.class);

  /**
   * 从 Nacos 配置中心读取的配置项
   * 这些配置可以在 Nacos 控制台动态修改
   */
  @Value("${app.name:user-service}")
  private String appName;

  @Value("${app.description:用户服务}")
  private String appDescription;

  @Value("${app.version:1.0.0}")
  private String appVersion;

  @Value("${feature.new-ui.enabled:false}")
  private Boolean newUiEnabled;

  @Value("${feature.cache.enabled:true}")
  private Boolean cacheEnabled;

  @Value("${feature.cache.ttl:300}")
  private Integer cacheTtl;

  /**
   * 获取当前配置信息
   * 
   * 测试步骤：
   * 1. 在 Nacos 控制台创建配置文件：user-service.yml
   * 2. 添加配置内容
   * 3. 调用此接口查看配置
   * 4. 在 Nacos 修改配置
   * 5. 再次调用此接口，配置会自动刷新
   */
  @GetMapping("/current")
  public Map<String, Object> getCurrentConfig() {
    Map<String, Object> config = new HashMap<>();

    config.put("appName", appName);
    config.put("appDescription", appDescription);
    config.put("appVersion", appVersion);

    Map<String, Object> features = new HashMap<>();
    features.put("newUiEnabled", newUiEnabled);
    features.put("cacheEnabled", cacheEnabled);
    features.put("cacheTtl", cacheTtl);
    config.put("features", features);

    config.put("refreshable", true);
    config.put("note", "这些配置来自 Nacos 配置中心，支持动态刷新");

    logger.info("当前配置: {}", config);
    return config;
  }

  /**
   * 获取配置管理指南
   */
  @GetMapping("/guide")
  public Map<String, Object> getConfigGuide() {
    Map<String, Object> guide = new HashMap<>();

    guide.put("title", "Nacos 配置管理使用指南");

    guide.put("step1_create_config", Map.of(
        "description", "在 Nacos 控制台创建配置",
        "dataId", "user-service.yml",
        "group", "COURSEHUB_GROUP",
        "namespace", "dev",
        "format", "YAML"));

    guide.put("step2_config_content", """
        # 示例配置内容
        app:
          name: user-service
          description: 用户服务
          version: 1.0.0

        feature:
          new-ui:
            enabled: false
          cache:
            enabled: true
            ttl: 300

        spring:
          datasource:
            url: jdbc:mysql://localhost:3306/user_db
            username: root
            password: 123456
        """);

    guide.put("step3_shared_config", Map.of(
        "description", "创建共享配置（多个服务共用）",
        "examples", Map.of(
            "common-database.yml", "数据库连接配置",
            "common-logging.yml", "日志配置",
            "common-redis.yml", "Redis 配置"),
        "group", "COMMON_GROUP"));

    guide.put("step4_refresh", Map.of(
        "description", "配置自动刷新",
        "mechanism", "使用 @RefreshScope 注解的 Bean 会在配置更新时自动刷新",
        "test", "在 Nacos 修改配置后，直接调用 /api/config/current 查看最新配置"));

    guide.put("best_practices", Map.of(
        "1", "敏感配置（密码等）应加密存储",
        "2", "使用命名空间隔离不同环境的配置",
        "3", "使用分组管理不同类型的配置",
        "4", "共享配置放在独立的 data-id 中",
        "5", "配置文件命名规范：{服务名}-{环境}.yml"));

    return guide;
  }

  /**
   * 模拟配置更新后的业务逻辑变化
   */
  @GetMapping("/feature-check")
  public Map<String, Object> checkFeatures() {
    Map<String, Object> result = new HashMap<>();

    // 根据配置决定功能开关
    if (newUiEnabled) {
      result.put("ui", "使用新版 UI");
    } else {
      result.put("ui", "使用旧版 UI");
    }

    if (cacheEnabled) {
      result.put("cache", "缓存已启用，TTL: " + cacheTtl + " 秒");
    } else {
      result.put("cache", "缓存已禁用");
    }

    result.put("message", "通过 Nacos 配置中心可以动态控制功能开关，无需重启服务");

    return result;
  }
}
