package com.sns.yourconnection.common.configuration;

import com.sns.yourconnection.security.oauth2.params.OAuth2ApiClient;
import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class OAuth2Config {

    @Bean
    public Map<OAuth2Provider, OAuth2ApiClient> oAuth2ApiClientMap(List<OAuth2ApiClient> clients) {
        return clients.stream().collect(
                Collectors.toUnmodifiableMap(OAuth2ApiClient::oAuthProvider, Function.identity())
        );
    }
}
