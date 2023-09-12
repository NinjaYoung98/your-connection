package com.sns.yourconnection.repository.redis;

import com.sns.yourconnection.common.properties.SmtpMailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
/**
 * @apiNote repository for Email - authentication
 * ttl : 5 minutes;
 */
public class EmailCertificationRepository {
    private final RedisTemplate<String, String> SmtpMailRedisTemplate;
    private final SmtpMailProperties properties;
    private static final String PREFIX_FOR_KEY = "SMTP: ";

    public void setValue(String toEmail, String securityCode) {
        String key = getKey(toEmail);
        SmtpMailRedisTemplate.opsForValue().set(key, securityCode, Duration.ofMillis(properties.getSecurityCodeExpirationMillis()));
        log.info("[SmtpMailRepository]Set security code for email to Redis: {}", toEmail);
    }

    public Optional<String> getValues(String email) {
        String value = SmtpMailRedisTemplate.opsForValue().get(getKey(email));
        return Optional.ofNullable(value);
    }

    private String getKey(String toEmail) {
        return PREFIX_FOR_KEY + toEmail;
    }
}
