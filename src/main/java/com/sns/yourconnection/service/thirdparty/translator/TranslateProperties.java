package com.sns.yourconnection.service.thirdparty.translator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "deep-l")
public class TranslateProperties {

    private String key;

    private String host;
}
