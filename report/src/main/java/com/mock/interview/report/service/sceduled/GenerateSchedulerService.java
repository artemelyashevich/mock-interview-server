package com.mock.interview.report.service.sceduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateSchedulerService {

    private static final String KEY_PREFIX = "interview-report-%s";

    private final RedisTemplate<String, String> redisTemplate;

    @Scheduled(cron = "0 */2 * * * ?")
    public void start() {

    }
}
