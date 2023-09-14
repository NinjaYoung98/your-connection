package com.sns.yourconnection.utils;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;


@RequiredArgsConstructor
public class AdminAccountUtil {

    private static final String ADMIN_PREFIX = "Admin";
    private static final String TITLE_MESSAGE = "[YourConnection] Don't expose your password";
    private static final int SECURITY_DIGIT_NUMBER = 6;
    private static final int RANDOM_NUMBER_BOUND = 10;

    public static String createAdminName() {
        return ADMIN_PREFIX + UUID.randomUUID();
    }

    public static String createAdminPassword() {
        return UUID.randomUUID() + createRandomCodeNumber();
    }

    private static String createRandomCodeNumber() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            return createRandomNumber(random);
        } catch (NoSuchAlgorithmException e) {
            throw new AppException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }

    private static String createRandomNumber(Random random) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < SECURITY_DIGIT_NUMBER; i++) {
            builder.append(random.nextInt(RANDOM_NUMBER_BOUND));
        }
        return builder.toString();
    }

    public static SimpleMailMessage createEmailForm(String toEmail, String adminName,
        String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(TITLE_MESSAGE);
        message.setText(getText(adminName, password));
        return message;
    }

    private static String getText(String adminName, String password) {
        return String.format("[Admin Account : %s], [Password : %s] ", adminName, password);
    }
}
