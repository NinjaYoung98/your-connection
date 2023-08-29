package com.sns.yourconnection.security.oauth2.params;

import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@NoArgsConstructor
public class KakaoLoginParams implements OAuth2LoginParams {
    private String authorizationCode;

    @Override
    public OAuth2Provider oAuth2Provider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> authorizationBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
