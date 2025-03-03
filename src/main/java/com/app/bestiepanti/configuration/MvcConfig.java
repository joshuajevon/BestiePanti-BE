package com.app.bestiepanti.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = new File("uploads/image/").getAbsolutePath();

        registry.addResourceHandler("/uploads/image/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
