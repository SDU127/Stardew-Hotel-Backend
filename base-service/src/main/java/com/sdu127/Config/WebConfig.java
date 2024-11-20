package com.sdu127.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 路由配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${pic.local-file-path}")
    String picLocalFilePath;
    @Value("${pic.virtual-path}")
    String picVirtualPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(picVirtualPath) //虚拟路径
                .addResourceLocations("file:" + picLocalFilePath) //绝对路径
                .resourceChain(true);
    }
}
