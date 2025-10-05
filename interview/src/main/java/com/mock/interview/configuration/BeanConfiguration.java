package com.mock.interview.configuration;

import com.mock.interview.lib.configuration.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public AppProperties appProperties() {
        return new ApplicationProps();
    }
}

