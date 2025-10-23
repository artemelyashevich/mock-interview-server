package com.mock.interview.auth2.service;

import com.mock.interview.lib.exception.MockInterviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisService redisService;

    public void checkOtp(String otp) {
        var key = "";
        var otpFromCache = redisService.getOtp(key);
        if (!otpFromCache.equals(otp)) {
            throw new MockInterviewException("Otp mismatch", 401);
        }

    }

    public String createOtp(String key) {
        var otp = String.format("%06d", new SecureRandom().nextInt(1000000));
        redisService.putOtp(key, otp);
        return otp;
    }
}
