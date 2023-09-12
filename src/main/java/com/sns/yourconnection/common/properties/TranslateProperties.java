package com.sns.yourconnection.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "deep-l")
public class TranslateProperties {

    private String key;

    private String host;
}
