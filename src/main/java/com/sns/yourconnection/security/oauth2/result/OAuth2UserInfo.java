package com.sns.yourconnection.security.oauth2.result;

import com.sns.yourconnection.security.oauth2.OAuth2Provider;

public interface OAuth2UserInfo {
    String getEmail();

    String getNickname();

    OAuth2Provider getOAuthProvider();

    String getId();
}
