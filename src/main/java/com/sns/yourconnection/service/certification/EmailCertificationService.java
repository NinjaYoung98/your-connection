package com.sns.yourconnection.service.certification;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.repository.UserRepository;
import com.sns.yourconnection.repository.redis.EmailCertificationRepository;
import com.sns.yourconnection.utils.files.EmailForms;
import com.sns.yourconnection.utils.generator.RandomCodeGenerator;
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
    private final UserRepository userRepository;
    private static final String TITLE_TO_SEND = "Your Connection 이메일 인증 번호";

    @Transactional
    public void sendCodeToEmail(String toEmail) {
        userRepository.findByEmail(toEmail)
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        EmailForms.validateEmailForm(toEmail);
        String securityCode = RandomCodeGenerator.createRandomCodeNumber();

        log.info("[EmailCertificationService] security code for "
            + "Email: {} has successfully created.", toEmail);

        emailCertificationRepository.setValue(toEmail, securityCode);
        sendEmail(toEmail, securityCode);
    }

    @Transactional
    public void verifiedCode(String email, String userCode) {
        String securityCode = emailCertificationRepository.getValues(email)
            .orElseThrow(() ->
            new AppException(ErrorCode.EXPIRED_VERIFICATION)
        );

        validateSecurityCode(userCode, securityCode);

        UserEntity userEntity = getUserEntityByEmail(email);
        userEntity.toVerified();

        if (userEntity.getUserActivity() == UserActivity.LOCKED) {
            userEntity.changeActivity(UserActivity.NORMAL);
            //TODO: User의 최신 변경 사항을 추적하여 변경하기 ref : https://kimji0139.tistory.com/94
        }
    }

    private static void validateSecurityCode(String userCode, String securityCode) {
        if (!securityCode.equals(userCode)) {
            throw new AppException(ErrorCode.INVALID_SECURITY_CODE);
        }
    }

    private UserEntity getUserEntityByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userEntity;
    }

    private void sendEmail(String toEmail, String securityCode) {
        SimpleMailMessage emailForm = EmailForms.createEmailForm(toEmail, TITLE_TO_SEND,
            securityCode);
        javaMailSender.send(emailForm);
    }
}
