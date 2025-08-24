package com.mock.interview.report.configuration;

import com.mock.interview.lib.configuration.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfiguration {

    private final AppProperties appProperties;

    @Bean(name = "reportGenerationExecutor")
    public ExecutorService reportGenerationExecutor() {
        return new ThreadPoolExecutor(
                appProperties.getReportGenerationCoreThreads(),
                appProperties.getReportGenerationMaxThreads(),
                appProperties.getReportGenerationKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(appProperties.getReportGenerationQueueCapacity()),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}