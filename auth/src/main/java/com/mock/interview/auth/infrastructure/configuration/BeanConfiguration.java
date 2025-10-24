package com.mock.interview.auth.infrastructure.configuration;

import com.mock.interview.lib.configuration.AppProperties;
import com.mock.interview.lib.security.SecurityHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AppProperties getAppProperties() {
        return new AppProperties();
    }

    @Bean
    public SecurityHelper getSecurityHelper() {
        return new SecurityHelper();
    }
}
