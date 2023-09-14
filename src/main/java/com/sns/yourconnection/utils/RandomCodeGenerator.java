package com.sns.yourconnection.utils;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class RandomCodeGenerator {

    private static final int SECURITY_DIGIT_NUMBER = 6;
    private static final int RANDOM_NUMBER_BOUND = 10;

    public static String createRandomCodeNumber() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            return getSecurityCode(random);
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

    private static String getSecurityCode(Random random) {
        StringBuilder builder = createRandomNumber(random);
        return builder.toString();
    }

    private static StringBuilder createRandomNumber(Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SECURITY_DIGIT_NUMBER; i++) {
            builder.append(random.nextInt(RANDOM_NUMBER_BOUND));
        }
        return builder;
    }
}
