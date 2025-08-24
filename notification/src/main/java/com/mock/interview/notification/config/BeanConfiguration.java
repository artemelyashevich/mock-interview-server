package com.mock.interview.notification.config;

import com.mock.interview.lib.configuration.AppProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class BeanConfiguration {

    @Bean
    public AppProperties appProperties() {
        return new AppProperties();
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
