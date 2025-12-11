package com.example.realestatehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 对所有接口生效
                registry.addMapping("/**")
                        // 允许前端请求的地址
                        .allowedOrigins("http://localhost:63344")
                        // 允许的 HTTP 方法
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        // 允许携带 cookie
                        .allowCredentials(true);
            }
        };
    }
}