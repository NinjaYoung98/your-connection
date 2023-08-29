package com.sns.yourconnection.security.oauth2.result;

import com.sns.yourconnection.security.oauth2.OAuth2Provider;

public interface OAuth2UserProfile {
    String getEmail();

    String getNickname();

    OAuth2Provider getOAuthProvider();

    String getId();
}
