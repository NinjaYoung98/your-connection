package com.sns.yourconnection.common.interceptor;

import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.service.thirdparty.telegram.TelegramService;
import com.sns.yourconnection.service.RateLimitService;
import com.sns.yourconnection.utils.HttpReqRespUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final Long BUCKET_CAPACITY = 1L;
    private final Long BUCKET_TOKENS = 1L;
    private final Duration CALLS_IN_SECONDS = Duration.ofSeconds(10);
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();
    private final RateLimitService rateLimitService;

    /**
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     * @apiNote `Retry-After` 설명:
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Retry-After
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {
        String clientIp = HttpReqRespUtil.getClientIp(request);
        Bucket bucket = cache.computeIfAbsent(clientIp, key -> newBucket());
        ConsumptionProbe consumptionProbe = bucket.tryConsumeAndReturnRemaining(1);
        if (isRateLimitExceeded(request, response, clientIp, consumptionProbe)) {
            return false;
        }
        return true;
    }

    /**
     * @apiNote rate limit 발생여부에 따른 각각의 success, error response 를 생성 및 반환 합니다. *
     * 'RateLimitCounter.checkLimitReachedThreshold()' 1시간에 10 번 이상의 rate limit 발생하는지 check 합니다.*
     */
    private boolean isRateLimitExceeded(HttpServletRequest request, HttpServletResponse response,
        String clientIp, ConsumptionProbe consumptionProbe) {
        if (!consumptionProbe.isConsumed()) {
            errorResponse(response, consumptionProbe);
            log.warn(
                "rate limit exceeded for client IP :{}  Refill in {} seconds  Request details: method = {} URI = {}"
                , clientIp, getRoundedSecondsToWaitForRefill(consumptionProbe), request.getMethod(),
                request.getRequestURI());

            // 만약에 1시간에 10번 이상의 Rate Limit 에러를 발생시키는 유저가 있다면 텔레그램 알림.
            rateLimitService.isLimitReachedThreshold(clientIp);
            return true;
        }
        successResponse(response, consumptionProbe);
        log.info("remaining token: {}", consumptionProbe.getRemainingTokens());
        return false;
    }

    private Bucket newBucket() {
        // 버킷의 총 크기 = 100 , 한 번에 충전되는 토큰 = 50 , 버킷 충전 시간 = 1초
        return Bucket.builder()
            .addLimit(Bandwidth.classic(
                BUCKET_CAPACITY, Refill.intervally(
                    BUCKET_TOKENS, CALLS_IN_SECONDS)))
            .build();
    }

    /**
     * @apiNote 'X-RateLimit-RetryAfter', 'X-RateLimit-Limit', 'X-RateLimit-Remaining' 참고:
     * https://sendbird.com/docs/chat/v3/platform-api/application/understanding-rate-limits/rate-limits
     */
    //TODO: response message ( custom response 객체 생성 후 구현 예정)
    private void successResponse(HttpServletResponse response, ConsumptionProbe consumptionProbe) {
        response.setHeader("X-RateLimit-Remaining",
            Long.toString(consumptionProbe.getRemainingTokens()));
        response.setHeader("X-RateLimit-Limit",
            BUCKET_CAPACITY + ";w=" + CALLS_IN_SECONDS.getSeconds());
    }

    private void errorResponse(HttpServletResponse response, ConsumptionProbe consumptionProbe) {
        response.setHeader("X-RateLimit-RetryAfter",
            Float.toString(getRoundedSecondsToWaitForRefill(consumptionProbe)));
        response.setHeader("X-RateLimit-Limit",
            BUCKET_CAPACITY + ";w=" + CALLS_IN_SECONDS.getSeconds());
        response.setStatus(ErrorCode.TOO_MANY_REQUESTS.getHttpStatus().value());
    }

    private float getRoundedSecondsToWaitForRefill(ConsumptionProbe consumptionProbe) {
        float secondsToWaitForRefill =
            (float) TimeUnit.NANOSECONDS.toMillis(consumptionProbe.getNanosToWaitForRefill())
                / 1000;
        return (float) Math.round(secondsToWaitForRefill * 10) / 10;
    }
}
