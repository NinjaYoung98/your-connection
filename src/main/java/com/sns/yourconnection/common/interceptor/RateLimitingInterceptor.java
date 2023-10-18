package com.sns.yourconnection.common.interceptor;

import static com.sns.yourconnection.utils.constants.RateLimitBucketConstants.*;

import com.sns.yourconnection.controller.response.ratelimit.RateLimitResponse;
import com.sns.yourconnection.service.protection.RateLimitService;
import com.sns.yourconnection.utils.checker.RateLimitRefillChecker;
import com.sns.yourconnection.utils.validation.ClientIpUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final RateLimitService rateLimitService;
    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) {
        String clientIp = ClientIpUtil.getClientIp(request);

        Bucket bucket = cache.computeIfAbsent(clientIp, key -> newBucket());
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(
            REQUEST_COST_IN_TOKENS);

        if (isRateLimitExceeded(request, response, clientIp, consumptionProbe)) {
            return false;
        }
        return true;
    }

    /**
     * @apiNote rate limit 발생여부에 따른 각각의 success, error response 를 생성 및 반환 합니다. *
     * 'rateLimitService.isLimitReachedThreshold(...)' 특정 IP에 대한 rate limit 허용치 초과 여부를 check 합니다.*
     */
    private boolean isRateLimitExceeded(HttpServletRequest request, HttpServletResponse response,
        String clientIp, ConsumptionProbe consumptionProbe) {

        if (!consumptionProbe.isConsumed()) {
            float waitForRefill =
                RateLimitRefillChecker.getRoundedSecondsToWaitForRefill(consumptionProbe);

            RateLimitResponse.errorResponse(
                response, BUCKET_CAPACITY, CALLS_IN_SECONDS, waitForRefill);

            log.warn(
                "rate limit exceeded for client IP :{}  Refill in {} seconds  Request "
                    + "details: method = {} URI = {}",
                clientIp, waitForRefill, request.getMethod(), request.getRequestURI());

            // 만약에 1시간에 10번 이상의 Rate Limit 에러를 발생시키는 유저가 있다면 알림 메시지.
            rateLimitService.isLimitReachedThreshold(clientIp);
            return true;
        }

        RateLimitResponse.successResponse(
            response, consumptionProbe.getRemainingTokens(), BUCKET_CAPACITY, CALLS_IN_SECONDS);

        log.info("remaining token: {}", consumptionProbe.getRemainingTokens());
        return false;
    }

    private Bucket newBucket() {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(
                BUCKET_CAPACITY, Refill.intervally(
                    BUCKET_TOKENS, CALLS_IN_SECONDS)))
            .build();
    }
}
