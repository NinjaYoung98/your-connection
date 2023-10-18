package com.sns.yourconnection.utils.checker;

import io.github.bucket4j.ConsumptionProbe;
import java.util.concurrent.TimeUnit;

public class RateLimitRefillChecker {

    public static float getRoundedSecondsToWaitForRefill(ConsumptionProbe consumptionProbe) {
        float secondsToWaitForRefill =
            (float) TimeUnit.NANOSECONDS.toMillis(
                consumptionProbe.getNanosToWaitForRefill()) / 1000;

        return (float) Math.round(secondsToWaitForRefill * 10) / 10;
    }
}
