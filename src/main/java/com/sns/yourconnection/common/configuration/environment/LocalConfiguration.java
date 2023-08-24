package com.sns.yourconnection.common.configuration.environment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Profile("local")
@RequiredArgsConstructor
@Configuration
@Slf4j
public class LocalConfiguration implements EnvConfiguration {

    private final Environment environment;

    @Override
    @Bean
    public void getMessage() {
        log.info("[" + environment.getProperty("spring.config.activate.on-profile") + "] environment is running");
    }
}
