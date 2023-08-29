package com.sns.yourconnection.service.thirdparty.oauth2;

import com.sns.yourconnection.exception.OAuth2RestClientException;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.user.UserEntity;
import com.sns.yourconnection.repository.UserRepository;
import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import com.sns.yourconnection.security.token.JwtTokenGenerator;
import com.sns.yourconnection.security.token.AccessToken;
import com.sns.yourconnection.security.oauth2.params.OAuth2ApiClient;
import com.sns.yourconnection.security.oauth2.params.OAuth2LoginParams;
import com.sns.yourconnection.security.oauth2.result.OAuth2UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final Map<OAuth2Provider, OAuth2ApiClient> oAuth2ApiClientMap;
    private final BCryptPasswordEncoder encoder;

    /**
     * @param params from OAuth2 resource server(KAKAO, NAVER...)
     * @return Access token generated JWT
     */
    public AccessToken login(OAuth2LoginParams params) {
        OAuth2UserProfile oAuth2UserProfile = requestUserProfile(params);
        return jwtTokenGenerator.generateAccessToken(getUsername(oAuth2UserProfile));
    }

    private String getUsername(OAuth2UserProfile oAuth2UserProfile) {
        //TODO: Naver 로그인 구현 후 해당 부분 정리하기
        String providerId = oAuth2UserProfile.getId();
        String username = oAuth2UserProfile.getOAuthProvider().name() + providerId;
        String dummyPassword = encoder.encode("{bcrypt}" + UUID.randomUUID());
        String nickName = oAuth2UserProfile.getNickname();

        return userRepository.findByUsername(username)
            .map(User::fromEntity)
            .map(User::getUsername)
            .orElseGet(() ->
                userRepository.save(UserEntity.of(username, dummyPassword, nickName))
                    .getUsername()
            );
    }

    public OAuth2UserProfile requestUserProfile(OAuth2LoginParams params) {
        OAuth2ApiClient oAuth2ApiClient = oAuth2ApiClientMap.get(params.oAuth2Provider());
        try {
            String accessToken = oAuth2ApiClient.requestAccessToken(params);
            OAuth2UserProfile oAuth2UserProfile = oAuth2ApiClient.requestUserProfile(accessToken);
            return oAuth2UserProfile;
        } catch (Exception e) {
            throw new OAuth2RestClientException("Failed to retrieve access token.");
        }
    }
}
