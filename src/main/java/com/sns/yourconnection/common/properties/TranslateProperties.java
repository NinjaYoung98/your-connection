package com.sns.yourconnection.common.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "deep-l")
public class TranslateProperties {

    private String key;

    private String host;
}
