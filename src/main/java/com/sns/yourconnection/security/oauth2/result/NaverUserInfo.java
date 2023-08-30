package com.sns.yourconnection.security.oauth2.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import lombok.Getter;

public class NaverUserInfo implements OAuth2UserInfo {

    @JsonProperty("response")
    private Response response;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Response {

        private String id;
        private String email;
        private String nickname;
    }

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public String getNickname() {
        return response.nickname;
    }

    @Override
    public OAuth2Provider getOAuthProvider() {
        return OAuth2Provider.NAVER;
    }

    public String getId() {
        return response.id;
    }
}
