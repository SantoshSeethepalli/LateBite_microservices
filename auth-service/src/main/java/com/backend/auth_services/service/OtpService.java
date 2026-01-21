package com.backend.auth_services.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redis;

    public OtpService(@Qualifier("redisTemplate") RedisTemplate<String, String> redis) {
        this.redis = redis;
    }

    public String generateOtp(String phone, String role) {

        String key = "otp:" + role + ":" + phone;
        String otp = String.format("%06d", new Random().nextInt(999999));

        redis.opsForValue().set(key, otp, Duration.ofMinutes(5));

        return otp;
    }

    public boolean verifyOtp(String phone, String role, String otp) {

        String key = "otp:" + role + ":" + phone;
        String real = redis.opsForValue().get(key);

        return real != null && real.equals(otp);
    }

    public void deleteOtp(String phone, String role) {
        redis.delete("otp:" + role + ":" + phone);
    }
}
