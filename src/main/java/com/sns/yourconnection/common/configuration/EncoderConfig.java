package com.sns.yourconnection.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncoderConfig {
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();         //spring 시큐리티와 BCryptPassword는 다른 클래스에 지정해주어야한다.(순환참조 발생 문제때문에)
    }
}
