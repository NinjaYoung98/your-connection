package com.sns.yourconnection.security.oauth2.params;

import com.sns.yourconnection.exception.OAuth2RestClientException;
import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import com.sns.yourconnection.security.oauth2.result.NaverUserInfo;
import com.sns.yourconnection.security.oauth2.result.OAuth2UserInfo;
import com.sns.yourconnection.security.token.NaverTokens;
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
public class NaverApiClient implements OAuth2ApiClient {

    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    /**
     * @param params params.authorizationBody(): has authorization-code for issued access token
     *               params.oAuth2Provider: inform provider
     * @return access token
     * @throws OAuth2RestClientException
     */
    @Override
    public String requestAccessToken(OAuth2LoginParams params) {
        //header
        HttpHeaders httpHeaders = createUrlEncodedHttpHeaders();
        //body
        MultiValueMap<String, String> body = params.authorizationBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        //request
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        //NaverTokens
        NaverTokens naverTokens = restTemplate.postForObject(tokenUri, request, NaverTokens.class);

        if (naverTokens == null) {
            throw new OAuth2RestClientException(
                "[NaverApiClient] Failed to retrieve access token.");
        }

        log.info("[NaverApiClient] naverTokens is successfully issued");
        return naverTokens.getAccessToken();
    }

    @Override
    public OAuth2UserInfo requestUserInfo(String accessToken) {
        //header
        HttpHeaders httpHeaders = createUrlEncodedHttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + accessToken);
        //body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //request
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        //response
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(userInfoUri,
            request, String.class);
        log.info("API response: stringResponseEntity.getBody(): {}",
            stringResponseEntity.getBody());
        return restTemplate.postForObject(userInfoUri, request, NaverUserInfo.class);
    }

    @Override
    public OAuth2Provider oAuthProvider() {
        return OAuth2Provider.NAVER;
    }

    private HttpHeaders createUrlEncodedHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return httpHeaders;
    }
}
