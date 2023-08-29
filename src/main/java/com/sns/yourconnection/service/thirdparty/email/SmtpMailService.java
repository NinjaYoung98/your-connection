package com.sns.yourconnection.service.thirdparty.email;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.redis.SmtpMailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmtpMailService {

    private final JavaMailSender javaMailSender;
    private final SmtpMailRepository smtpMailRepository;
    private static final String TITLE_TO_SEND = "Your Connection 이메일 인증 번호";
    private static final int SECURITY_DIGIT_NUMBER = 6;
    private static final int RANDOM_NUMBER_BOUND = 10;


    @Transactional
    public void sendCodeToEmail(String toEmail) {
        validteEmail(toEmail);
        String securityCode = createSecurityCode();
        log.info("[SmtpMailService] security code for Email: {} has successfully created.",
            toEmail);
        smtpMailRepository.setValue(toEmail, securityCode);
        sendEmail(toEmail, securityCode);
    }


    private void sendEmail(String toEmail, String securityCode) {
        SimpleMailMessage emailForm = createEmailForm(toEmail, securityCode);
        javaMailSender.send(emailForm);
    }

    private String createSecurityCode() {
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
    // 발신할 이메일 데이터 세팅

    private SimpleMailMessage createEmailForm(String toEmail, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(TITLE_TO_SEND);
        message.setText(text);
        log.info("[SmtpMailService] Create email form successfully.");
        return message;
    }

    private void validteEmail(String email) {
        // 이메일 형식에 대한 정규식 검증  ex) username@domaim.com
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        if (!pattern.matcher(email).matches()) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT);
        }
    }
}
