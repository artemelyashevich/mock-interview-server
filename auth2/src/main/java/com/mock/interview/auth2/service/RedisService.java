package com.mock.interview.auth2.service;

import com.mock.interview.auth2.configuration.AppProps;
import com.mock.interview.lib.exception.MockInterviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AppProps appProps;

    public void putOtp(String keyUnique, String otp) {
        redisTemplate.opsForValue().set("otp:%s".formatted(keyUnique), otp, appProps.getOtpTtl(), TimeUnit.SECONDS);
    }

    public String getOtp(String keyUnique) {
        var otp = redisTemplate.opsForValue().get("otp:%s".formatted(keyUnique));
        if (otp == null) {
            throw new MockInterviewException("Otp expired", 400);
        }
        return otp;
    }
}
