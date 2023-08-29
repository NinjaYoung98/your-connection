package com.sns.yourconnection.security.oauth2.params;

import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import org.springframework.util.MultiValueMap;

public interface OAuth2LoginParams {
    OAuth2Provider oAuth2Provider();

    MultiValueMap<String, String> authorizationBody();
}
