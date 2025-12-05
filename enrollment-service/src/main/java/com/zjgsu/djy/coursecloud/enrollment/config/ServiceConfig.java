package com.zjgsu.djy.coursecloud.enrollment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services")
@Data
public class ServiceConfig {
    
    private UserService user;
    private CatalogService catalog;
    
    @Data
    public static class UserService {
        private String url;
    }
    
    @Data
    public static class CatalogService {
        private String url;
    }
}

