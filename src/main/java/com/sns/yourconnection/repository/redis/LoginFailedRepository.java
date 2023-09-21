package com.sns.yourconnection.repository.redis;

import com.sns.yourconnection.utils.validation.ClassUtil;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoginFailedRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Long TIME_TO_LIVE = 86400000L;
    private static final String PREFIX_FOR_KEY = "ULF: ";

    public void setValue(String username, Integer failedCount) {
        String key = getKey(username);
        redisTemplate.opsForValue().set(key, failedCount, Duration.ofMillis(TIME_TO_LIVE));
        log.info("[LoginFailedRepository]count login failed for user : {}", username);
    }

    public Integer getValues(String username) {
        Integer failedCount = ClassUtil.castingInstance(
            redisTemplate.opsForValue().get(getKey(username)), Integer.class);
        return failedCount;
    }

    private String getKey(String username) {
        return PREFIX_FOR_KEY + username;
    }

    public Long increment(String username) {
        return redisTemplate.opsForValue().increment(getKey(username));
    }

    public void delete(String username) {
        redisTemplate.delete(getKey(username));
    }
}
