package com.project.TalentFindr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map all URLs like /files/** to the actual file path
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:D:/desktop2.0/jobportal/uploaded-files/");
    }

    // anything having a http url like /files should be mapped to url like like D: on the local disk .
}
