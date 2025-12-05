package com.zjgsu.djy.coursecloud.user.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 优雅关闭配置
 * 实现服务优雅上下线：
 * 1. 从 Nacos 注销服务实例
 * 2. 等待正在处理的请求完成
 * 3. 关闭服务
 */
@Configuration
public class GracefulShutdownConfig {

  private static final Logger logger = LoggerFactory.getLogger(GracefulShutdownConfig.class);

  /**
   * 优雅关闭处理器
   */
  @Component
  public static class GracefulShutdownHandler {

    @Autowired(required = false)
    private NacosAutoServiceRegistration nacosAutoServiceRegistration;

    @Autowired
    private ApplicationContext applicationContext;

    // 优雅关闭等待时间（秒）
    private static final int GRACEFUL_SHUTDOWN_TIMEOUT = 30;

    /**
     * 在 JVM 关闭前执行优雅关闭逻辑
     */
    @PreDestroy
    public void gracefulShutdown() {
      logger.info("========================================");
      logger.info("开始执行优雅关闭流程...");
      logger.info("========================================");

      try {
        // 步骤1: 从 Nacos 注销服务
        deregisterFromNacos();

        // 步骤2: 等待一段时间，让客户端感知到服务下线
        waitForClientAwareness();

        // 步骤3: 等待正在处理的请求完成
        waitForRequestsToComplete();

        logger.info("========================================");
        logger.info("优雅关闭流程完成");
        logger.info("========================================");

      } catch (Exception e) {
        logger.error("优雅关闭过程中发生错误", e);
      }
    }

    /**
     * 从 Nacos 注销服务实例
     */
    private void deregisterFromNacos() {
      try {
        if (nacosAutoServiceRegistration != null) {
          logger.info("正在从 Nacos 注销服务实例...");
          nacosAutoServiceRegistration.stop();
          logger.info("✓ 成功从 Nacos 注销服务实例");
        } else {
          logger.warn("⚠ NacosAutoServiceRegistration 未找到，跳过注销步骤");
        }
      } catch (Exception e) {
        logger.error("✗ 从 Nacos 注销失败", e);
      }
    }

    /**
     * 等待客户端感知到服务下线
     * 给 Nacos 客户端一些时间来更新服务列表
     */
    private void waitForClientAwareness() {
      int waitSeconds = 5;
      logger.info("等待 {} 秒，让客户端感知服务下线...", waitSeconds);
      try {
        TimeUnit.SECONDS.sleep(waitSeconds);
        logger.info("✓ 客户端感知等待完成");
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.warn("等待过程被中断");
      }
    }

    /**
     * 等待正在处理的请求完成
     */
    private void waitForRequestsToComplete() {
      logger.info("等待正在处理的请求完成（最多 {} 秒）...", GRACEFUL_SHUTDOWN_TIMEOUT);

      try {
        // 获取 Tomcat 连接器的活动连接数
        if (applicationContext instanceof ServletWebServerApplicationContext) {
          ServletWebServerApplicationContext webServerContext = (ServletWebServerApplicationContext) applicationContext;

          if (webServerContext.getWebServer() instanceof TomcatWebServer) {
            TomcatWebServer tomcatServer = (TomcatWebServer) webServerContext.getWebServer();

            int waitTime = 0;
            int checkInterval = 1; // 每秒检查一次

            // 等待活动请求完成，或超时
            while (waitTime < GRACEFUL_SHUTDOWN_TIMEOUT) {
              // 实际生产中可以检查 Tomcat 的活动连接数
              // 这里简化处理，等待固定时间
              TimeUnit.SECONDS.sleep(checkInterval);
              waitTime += checkInterval;

              // 可以添加检查逻辑，如果没有活动请求则提前退出
              logger.debug("已等待 {} 秒，继续等待请求完成...", waitTime);
            }

            logger.info("✓ 请求处理等待完成（共等待 {} 秒）", waitTime);
          }
        }
      } catch (Exception e) {
        logger.warn("等待请求完成时发生错误", e);
      }
    }
  }
}
