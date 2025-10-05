package com.mock.interview.configuration;

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
public class ApplicationProps extends AppProperties {

    private WebClient webClient = new WebClient();

    private boolean isExternalAi = false;

    private String externalAiUrl;

    @Getter
    @Setter
    public static class WebClient {
        private Integer maxTotal = 30;
        private Integer maxPerRoute = 10;
        private Integer readTimeoutMs = 60000;
        private Integer connectionTimeoutMs = 2000;
        private Integer connectionTimeToLiveMs = 10000;
        private Integer connectionValidateAfterInactivityMs = 500;
        private Integer retryCount = 5;
    }
}
