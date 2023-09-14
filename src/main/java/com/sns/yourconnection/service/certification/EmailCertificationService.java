package com.sns.yourconnection.service.certification;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.redis.EmailCertificationRepository;
import com.sns.yourconnection.utils.EmailForms;
import com.sns.yourconnection.utils.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCertificationService {

    private final JavaMailSender javaMailSender;
    private final EmailCertificationRepository emailCertificationRepository;
    private static final String TITLE_TO_SEND = "Your Connection 이메일 인증 번호";

    @Transactional
    public void sendCodeToEmail(String toEmail) {
        EmailForms.validateEmailForm(toEmail);
        String securityCode = RandomCodeGenerator.createRandomCodeNumber();
        log.info("[SmtpMailService] security code for Email: {} has successfully created.",
            toEmail);
        emailCertificationRepository.setValue(toEmail, securityCode);
        sendEmail(toEmail, securityCode);
    }

    @Transactional(readOnly = true)
    public void verifiedCode(String email, String userCode) {
        String securityCode = emailCertificationRepository.getValues(email).orElseThrow(() ->
            new AppException(ErrorCode.EXPIRED_VERIFICATION)
        );
        if (!securityCode.equals(userCode)) {
            throw new AppException(ErrorCode.INVALID_SECURITY_CODE);
        }
    }

    private void sendEmail(String toEmail, String securityCode) {
        SimpleMailMessage emailForm = EmailForms.createEmailForm(toEmail, TITLE_TO_SEND,
            securityCode);
        log.info("[SmtpMailService] Create email form successfully.");
        javaMailSender.send(emailForm);
    }
}
