package com.sns.yourconnection.service.users.admin;


import static com.sns.yourconnection.utils.generator.AdminAccountUtil.*;

import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCreationService {

    @Value("${project.mail}")
    private String projectDefaultMail;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;


//    @PostConstruct
//    public void initAdmin() {
//        if (userRepository.findByEmail(projectDefaultMail).isEmpty()) {
//            createAccount(projectDefaultMail);
//        }
//    }

    @Transactional
    public void createAccount(String email) {
        String adminName = createAdminName();
        String password = createAdminPassword();

        UserEntity userEntity = UserEntity.of(
            adminName, encoder.encode(password), "admin", email);
        userEntity.toAdmin();
        userRepository.save(userEntity);

        javaMailSender.send(createEmailForm(email, adminName, password));
    }
}
