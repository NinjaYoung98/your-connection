package com.sns.yourconnection.utils.constants;

import java.time.Duration;

/**
 * @apiNote # BUCKET_CAPACITY : 버킷의 총 크기 (용량)
 *          # BUCKET_TOKENS : 시간당 버킷안에 충전되는 토큰의 수
 *          # CALLS_IN_SECONDS : 버킷 충전 시간
 *          # REQUEST_COST_IN_TOKENS : 1회 요청당 소비되는 토큰 수
 */
public class RateLimitBucketConstants {

    public static final Long BUCKET_CAPACITY = 10L;
    public static final Long BUCKET_TOKENS = 10L;
    public static final Duration CALLS_IN_SECONDS = Duration.ofSeconds(1);
    public static final Integer REQUEST_COST_IN_TOKENS = 1;

}
