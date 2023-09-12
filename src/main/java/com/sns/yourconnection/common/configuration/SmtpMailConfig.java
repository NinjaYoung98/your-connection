package com.sns.yourconnection.common.configuration;

import com.sns.yourconnection.common.properties.SmtpMailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SmtpMailConfig {

    private final SmtpMailProperties smtpMailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpMailProperties.getHost());
        mailSender.setPort(smtpMailProperties.getPort());
        mailSender.setUsername(smtpMailProperties.getUsername());
        mailSender.setPassword(smtpMailProperties.getPassword());
        mailSender.setDefaultEncoding(smtpMailProperties.getEncode());
        mailSender.setJavaMailProperties(getProperties());
        return mailSender;
    }

    /**
     * @apiNote Properties key 값 ( mail.smtp) 참고:
     * https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html#package.description
     */
    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", smtpMailProperties.isAuth());
        properties.put("mail.smtp.starttls.enable", smtpMailProperties.isStarttlsEnable());
        properties.put("mail.smtp.starttls.required", smtpMailProperties.isStarttlsRequired());
        properties.put("mail.smtp.connectiontimeout", smtpMailProperties.getConnectionTimeout());
        properties.put("mail.smtp.timeout", smtpMailProperties.getTimeout());
        properties.put("mail.smtp.writetimeout", smtpMailProperties.getWriteTimeout());
        return properties;
    }
}
