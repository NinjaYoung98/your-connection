package com.sns.yourconnection.service.thirdparty.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "telegram-v2")
public class TelegramProperties {

    private String url;

    private String chatId;

}