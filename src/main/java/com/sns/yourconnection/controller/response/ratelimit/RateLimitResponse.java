package com.sns.yourconnection.controller.response.ratelimit;

import com.sns.yourconnection.exception.ErrorCode;
import java.time.Duration;
import javax.servlet.http.HttpServletResponse;

public class RateLimitResponse {

    /**
     * @apiNote 'X-RateLimit-RetryAfter', 'X-RateLimit-Limit', 'X-RateLimit-Remaining' 참고:
     * https://sendbird.com/docs/chat/v3/platform-api/application/understanding-rate-limits/rate-limits
     * @apiNote `Retry-After` 참고:
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Retry-After
     */
    public static void successResponse(HttpServletResponse response,
        long remainingTokens, Long bucketCapacity, Duration callsInSeconds) {

        response.setHeader("X-RateLimit-Remaining",
            Long.toString(remainingTokens));

        response.setHeader("X-RateLimit-Limit",
            bucketCapacity + ";w=" + callsInSeconds.getSeconds());
    }

    public static void errorResponse(HttpServletResponse response,
        Long bucketCapacity, Duration callsInSeconds, float waitForRefill) {

        response.setHeader("X-RateLimit-RetryAfter",
            Float.toString(waitForRefill));

        response.setHeader("X-RateLimit-Limit",
            bucketCapacity + ";w=" + callsInSeconds.getSeconds());

        response.setStatus(ErrorCode.TOO_MANY_REQUESTS.getHttpStatus().value());
    }
}
