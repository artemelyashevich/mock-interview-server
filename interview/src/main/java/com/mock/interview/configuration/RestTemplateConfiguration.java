package com.mock.interview.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfiguration {

    private final ApplicationProps appProperties;

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        var result = new PoolingHttpClientConnectionManager();
        result.setMaxTotal(appProperties.getWebClient().getMaxTotal());
        result.setDefaultMaxPerRoute(appProperties.getWebClient().getMaxPerRoute());
        result.setValidateAfterInactivity(appProperties.getWebClient().getConnectionValidateAfterInactivityMs());
        return result;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(appProperties.getWebClient().getReadTimeoutMs())
                .setConnectTimeout(appProperties.getWebClient().getConnectionTimeoutMs())
                .setSocketTimeout(appProperties.getWebClient().getReadTimeoutMs())
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
                                          RequestConfig requestConfig) {
        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setConnectionTimeToLive(appProperties.getWebClient().getConnectionTimeToLiveMs(), TimeUnit.MILLISECONDS)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(appProperties.getWebClient().getRetryCount(), true, new ArrayList<>()) {
                    @Override
                    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                        log.info("Retry request, execution count: '{}', exception: '{}'", executionCount, exception);
                        return super.retryRequest(exception, executionCount, context);
                    }
                }).build();
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient((org.apache.hc.client5.http.classic.HttpClient) httpClient);
        return new RestTemplate(requestFactory);
    }
}
