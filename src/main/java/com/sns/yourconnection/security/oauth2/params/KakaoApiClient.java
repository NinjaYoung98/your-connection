package com.sns.yourconnection.security.oauth2.params;

import com.sns.yourconnection.exception.OAuth2RestClientException;
import com.sns.yourconnection.security.oauth2.*;
import com.sns.yourconnection.security.oauth2.result.KakaoUserProfile;
import com.sns.yourconnection.security.oauth2.result.OAuth2UserProfile;
import com.sns.yourconnection.security.token.KakaoTokens;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoApiClient implements OAuth2ApiClient {
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    private final RestTemplate restTemplate;

    /**
     * @param params params.authorizationBody(): has authorization-code for issued access token
     *               params.oAuth2Provider: inform provider
     * @return access token
     * @throws OAuth2RestClientException
     */

    @Override
    public String requestAccessToken(OAuth2LoginParams params) throws OAuth2RestClientException {
        // header
        HttpHeaders httpHeaders = createUrlEncodedHttpHeaders();
        // body
        MultiValueMap<String, String> authorizationBody = params.authorizationBody();
        authorizationBody.add("grant_type", GRANT_TYPE);
        authorizationBody.add("client_id", clientId);
        //request
        HttpEntity<?> request = new HttpEntity<>(authorizationBody, httpHeaders);
        //kakaoTokens
        KakaoTokens kakaoTokens = restTemplate.postForObject(tokenUri, request, KakaoTokens.class);

        if (kakaoTokens == null) {
            throw new OAuth2RestClientException("[KakaoApiClient] Failed to retrieve access token.");
        }
        log.info("[KakaoApiClient] KakaoTokens is successfully issued");
        return kakaoTokens.getAccessToken();
    }

    @Override
    public OAuth2UserProfile requestUserProfile(String accessToken) {
        // header
        HttpHeaders httpHeaders = createUrlEncodedHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        // body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");
        //request
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        //response
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(userInfoUri, request, String.class);
        log.info("API response: stringResponseEntity.getBody(): {}", stringResponseEntity.getBody());
        KakaoUserProfile kakaoUserProfile = restTemplate.postForObject(userInfoUri, request, KakaoUserProfile.class);
        return kakaoUserProfile;
    }

    @Override
    public OAuth2Provider oAuthProvider() {
        return OAuth2Provider.KAKAO;
    }

    private HttpHeaders createUrlEncodedHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
