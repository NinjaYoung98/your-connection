package com.sns.yourconnection;

import com.sns.yourconnection.common.properties.AwsProperties;
import com.sns.yourconnection.common.properties.SmtpMailProperties;
import com.sns.yourconnection.common.properties.TelegramProperties;
import com.sns.yourconnection.common.properties.TranslateProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {TelegramProperties.class, TranslateProperties.class,
    SmtpMailProperties.class, AwsProperties.class})
public class YourConnectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(YourConnectionApplication.class, args);
    }

}
