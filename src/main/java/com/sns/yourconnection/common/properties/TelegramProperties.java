package com.sns.yourconnection.common.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "telegram-v2")
public class TelegramProperties {

    private String url;

    private String chatId;

}