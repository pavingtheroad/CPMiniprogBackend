package com.changping.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCconfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.uploadRepair-dir}")
    private String uploadRepairDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploadimage/**")
                .addResourceLocations("file:" + uploadDir);
        registry.addResourceHandler("/uploadrepair/**")
                .addResourceLocations("file:" + uploadRepairDir);
    }
}

