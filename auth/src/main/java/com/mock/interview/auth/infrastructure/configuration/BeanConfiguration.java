package com.mock.interview.auth.infrastructure.configuration;

import com.mock.interview.lib.configuration.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AppProperties getAppProperties() {
        return new AppProperties();
    }
}
