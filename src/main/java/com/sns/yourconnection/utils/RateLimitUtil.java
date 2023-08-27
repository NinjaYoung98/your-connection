package com.sns.yourconnection.utils;

import com.sns.yourconnection.service.thirdparty.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitUtil {
    private Map<String, RequestInfo> rateLimitErrorCounter = new ConcurrentHashMap<>();
    private final TelegramService telegramService;

    public void checkLimitReachedThreshold(String clientIp) {
        RequestInfo requestInfo = rateLimitErrorCounter.computeIfAbsent(clientIp, key -> new RequestInfo());
        if (!isApplyHandling(clientIp, requestInfo)) {
            requestInfo.saveCount();
        }
    }

    private boolean isApplyHandling(String clientIp, RequestInfo requestInfo) {
        if (requestInfo.isWithinTimeWindow()) {
            int count = requestInfo.incrementAndGetCount();
            log.info("[Rate limit count] client IP : {}  limit count  : {} ", clientIp, count);
            RateLimitHandling(clientIp, requestInfo, count);
            return true;
        }
        return false;
    }

    private void RateLimitHandling(String clientIp, RequestInfo requestInfo, int count) {
        if (count >= 10) {
            alertRateLimit(clientIp);
            requestInfo.resetCount();
        }
    }

    private void alertRateLimit(String clientIp) {
        telegramService.sendTelegram(String.format(" Rate limit is occurred 10 or more times for this client IP: %s", clientIp));
    }

    private static class RequestInfo {
        private AtomicInteger count = new AtomicInteger(0);
        private LocalDateTime lastRequestTime;

        public int incrementAndGetCount() {
            return this.count.incrementAndGet();
        }

        public boolean isWithinTimeWindow() {
            LocalDateTime now = LocalDateTime.now();
            if (lastRequestTime == null || ChronoUnit.HOURS.between(lastRequestTime, now) >= 1) {
                lastRequestTime = now;
                return false;
            }
            return true;
        }

        public void saveCount() {
            count.set(1);
            lastRequestTime = LocalDateTime.now();
        }

        public void resetCount() {
            count.set(0);
            lastRequestTime = LocalDateTime.now();
        }
    }
}
