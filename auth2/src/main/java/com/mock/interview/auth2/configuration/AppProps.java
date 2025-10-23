package com.mock.interview.auth2.configuration;

import com.mock.interview.lib.configuration.AppProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
@Validated
@Component
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableConfigurationProperties(AppProperties.class)
public class AppProps extends AppProperties {

    private int otpTtl;
}
