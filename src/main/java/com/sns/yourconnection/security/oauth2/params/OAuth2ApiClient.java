package com.sns.yourconnection.security.oauth2.params;

import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import com.sns.yourconnection.security.oauth2.result.OAuth2UserInfo;

public interface OAuth2ApiClient {
    OAuth2Provider oAuthProvider();
    String requestAccessToken(OAuth2LoginParams params) throws Exception;
    OAuth2UserInfo requestUserInfo(String accessToken);
}
