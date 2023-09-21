package com.sns.yourconnection.service.certification;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.exception.OAuth2RestClientException;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.repository.UserRepository;
import com.sns.yourconnection.security.oauth2.OAuth2Provider;
import com.sns.yourconnection.security.token.JwtTokenGenerator;
import com.sns.yourconnection.security.token.AccessToken;
import com.sns.yourconnection.security.oauth2.params.OAuth2ApiClient;
import com.sns.yourconnection.security.oauth2.params.OAuth2LoginParams;
import com.sns.yourconnection.security.oauth2.result.OAuth2UserInfo;
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
        OAuth2UserInfo oAuth2UserInfo = requestUserInfo(params);
        return jwtTokenGenerator.generateAccessToken(findUser(oAuth2UserInfo));
    }

    private String findUser(OAuth2UserInfo oAuth2UserInfo) {
        String username = getUsernameByUserInfo(oAuth2UserInfo);

        log.info("[OAuth2LoginService] Find user by username : {}", username);

        return userRepository.findByUsername(username)
            .map(User::fromEntity)
            .map(User::getUsername)
            .orElseGet(
                () -> createUserInfo(oAuth2UserInfo, username)
            );
    }

    public OAuth2UserInfo requestUserInfo(OAuth2LoginParams params) {
        OAuth2ApiClient oAuth2ApiClient = oAuth2ApiClientMap.get(params.oAuth2Provider());
        try {
            String accessToken = oAuth2ApiClient.requestAccessToken(params);
            return oAuth2ApiClient.requestUserInfo(accessToken);

        } catch (Exception e) {
            throw new OAuth2RestClientException("Failed to retrieve access token.");
        }
    }

    private String createUserInfo(OAuth2UserInfo oAuth2UserInfo, String username) {
        String dummyPassword = encoder.encode("{bcrypt}" + UUID.randomUUID());
        String nickname = oAuth2UserInfo.getNickname();
        String email = oAuth2UserInfo.getEmail();
        // 이미 가입된 이메일 계정이 있다면 계정을 생성할 수 없습니다.
        userRepository.findByEmail(email).ifPresent(userEntity -> {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL, "이미 가입된 계정이 있습니다.");
        });

        return userRepository.save(
                UserEntity.of(
                    username, dummyPassword, nickname, email))
            .getUsername();
    }

    private String getUsernameByUserInfo(OAuth2UserInfo oAuth2UserInfo) {
        String providerId = oAuth2UserInfo.getId();
        String username = "[" + oAuth2UserInfo.getOAuthProvider().name() + "]" + providerId;
        return username;
    }
}
