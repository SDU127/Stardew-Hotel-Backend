package com.sdu127.Config;

import com.sdu127.Filter.BusRefreshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<BusRefreshFilter> filterRegistration() {
        FilterRegistrationBean<BusRefreshFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new BusRefreshFilter());
        registration.setUrlPatterns(Collections.singletonList("/*"));
        registration.setName("BusRefreshFilter");
        registration.setOrder(1);
        return registration;
    }
}
