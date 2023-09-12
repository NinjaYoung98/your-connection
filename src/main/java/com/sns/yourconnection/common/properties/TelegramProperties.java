package com.sns.yourconnection.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "telegram-v2")
public class TelegramProperties {

    private String url;

    private String chatId;

}