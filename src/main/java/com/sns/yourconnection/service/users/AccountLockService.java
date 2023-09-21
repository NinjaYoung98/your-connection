package com.sns.yourconnection.service.users;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.repository.UserRepository;
import com.sns.yourconnection.utils.files.EmailForms;
import com.sns.yourconnection.utils.generator.RandomCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AccountLockService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private static final String USER_AUTHENTICATION_MESSAGE = "Enter the following verification code";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void lockUserAccount(User user) {

        /*
            계정 잠금 조치
                - 계정 activity 값을 locked 로 변경 (추가적인 인증 필요한 상태)
                - 계정 가입한 이메일로 보안 코드 발송
         */

        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userEntity.changeActivity(UserActivity.LOCKED);
        userEntity.toUnVerified();
        javaMailSender.send(EmailForms.createEmailForm(user.getEmail(), USER_AUTHENTICATION_MESSAGE,
            RandomCodeGenerator.createRandomCodeNumber()));
    }
}
