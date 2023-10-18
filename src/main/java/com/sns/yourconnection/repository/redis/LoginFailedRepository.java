package com.sns.yourconnection.repository.redis;

import com.sns.yourconnection.utils.loader.LuaScriptLoader;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoginFailedRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX_FOR_KEY = "ULF: ";
    private static final Integer INIT_LOGIN_TRIAL_COUNT = 1;
    private static final Integer MAX_ATTEMPT_COUNT = 5;

    @Value("${lua.login-failed}")
    private String luaPath;

    private String getKey(String username) {
        return PREFIX_FOR_KEY + username;
    }


    public boolean checkLockAccountByKey(String username) {
        String luaScript = LuaScriptLoader.load(luaPath);
        Boolean shouldLockAccount = redisTemplate.execute(
            new DefaultRedisScript<>(luaScript, Boolean.class),
            Collections.singletonList(getKey(username)),
            MAX_ATTEMPT_COUNT.toString(), INIT_LOGIN_TRIAL_COUNT.toString());

        return shouldLockAccount;
    }
}
