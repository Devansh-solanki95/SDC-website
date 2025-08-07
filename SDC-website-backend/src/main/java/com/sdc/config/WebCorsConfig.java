package com.sdc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // 🔓 Public: /auth/**
        registry.addMapping("/auth/**")
                .allowedOrigins("https://sdc-front-copy-dyc1.vercel.app")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        // 🔓 Public: /public/**
        registry.addMapping("/public/**")
                .allowedOrigins("https://sdc-front-copy-dyc1.vercel.app")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);

        // 🔐 Admin: /admin/**
        registry.addMapping("/admin/**")
                .allowedOrigins("https://sdc-front-copy-dyc1.vercel.app") // replace with actual admin frontend domain in production
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // for JWT auth header or session cookies
    }
}
