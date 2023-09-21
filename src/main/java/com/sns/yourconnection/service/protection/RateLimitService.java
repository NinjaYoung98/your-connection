package com.sns.yourconnection.service.protection;

import com.sns.yourconnection.service.notifitation.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private Map<String, RequestInfo> rateLimitErrorCounter = new ConcurrentHashMap<>();
    private final NotificationService notificationService;

    public boolean isLimitReachedThreshold(String clientIp) {
        RequestInfo requestInfo = rateLimitErrorCounter.computeIfAbsent(clientIp,
            key -> new RequestInfo());

        if (!isApplyHandling(clientIp, requestInfo)) {
            requestInfo.saveCount();
            return false;
        }
        return true;
    }

    private boolean isApplyHandling(String clientIp, RequestInfo requestInfo) {
        if (requestInfo.isWithinTimeWindow()) {
            int count = requestInfo.incrementAndGetCount();

            log.info("[Rate limit count] client IP : {}  limit count  : {} ", clientIp, count);

            checkAndResetIfLimitExceeded(clientIp, requestInfo, count);
            return true;
        }
        return false;
    }

    private void checkAndResetIfLimitExceeded(String clientIp, RequestInfo requestInfo, int count) {
        if (count >= 10) {
            requestInfo.resetCount();
            notificationService.sendMessage(
                String.format(" Rate limit is occurred 10 or more times for this client IP: %s",
                    clientIp));
        }
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
