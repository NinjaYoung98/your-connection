package com.sns.yourconnection.controller;

import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.param.certification.EmailVerifyRequest;
import com.sns.yourconnection.model.param.users.UserJoinRequest;
import com.sns.yourconnection.model.param.users.UserLoginRequest;
import com.sns.yourconnection.model.result.users.UserJoinResponse;
import com.sns.yourconnection.model.result.users.UserLoginResponse;
import com.sns.yourconnection.security.oauth2.params.KakaoLoginParams;
import com.sns.yourconnection.security.oauth2.params.NaverLoginParams;
import com.sns.yourconnection.security.token.AccessToken;
import com.sns.yourconnection.service.users.UserService;
import com.sns.yourconnection.service.certification.EmailCertificationService;
import com.sns.yourconnection.service.certification.OAuth2LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import static com.sns.yourconnection.controller.response.ResponseSuccess.response;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/users")
@Slf4j
public class UserPublicApiController {

    private final UserService userService;
    private final EmailCertificationService mailService;
    private final OAuth2LoginService oAuth2LoginService;

    @PostMapping("/join")
    public ResponseSuccess<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {

        log.info("create a new User Request Details: username: {}, nickname: {}",
            userJoinRequest.getUsername(), userJoinRequest.getNickname());

        User user = userService.join(userJoinRequest);
        return response(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/emails/verification-requests")
    public ResponseSuccess<Void> sendMessage(@RequestParam String email) {

        log.info("send a message to email : {} ", email);

        mailService.sendCodeToEmail(email);
        return response();
    }

    @PostMapping("/emails/verifications")
    public ResponseSuccess<Void> verificationEmail(@RequestParam String email,
        @RequestBody EmailVerifyRequest emailVerifyRequest) {

        log.info("verify email : {}  by security code ", email);

        mailService.verifiedCode(email, emailVerifyRequest.getSecurityCode());
        log.info("verify for email has been successfully completed ");

        return response();
    }

    @PostMapping("/login")
    public ResponseSuccess<UserLoginResponse> login(
        @RequestBody UserLoginRequest userLoginRequest) {

        log.info("User is attempting to login with username: {}", userLoginRequest.getUsername());

        AccessToken accessToken = userService.login(userLoginRequest);
        return response(UserLoginResponse.of(accessToken));
    }

    @PostMapping("/login/kakao")
    public ResponseSuccess<UserLoginResponse> loginKakao(@RequestBody KakaoLoginParams params) {

        log.info("[UserPublicApiController] KakaoLoginParams : {} ", params.getAuthorizationCode());

        AccessToken accessToken = oAuth2LoginService.login(params);
        return response(UserLoginResponse.of(accessToken));
    }

    @PostMapping("/login/naver")
    public ResponseSuccess<UserLoginResponse> loginNaver(@RequestBody NaverLoginParams params) {

        log.info("[UserPublicApiController] NaverLoginParams : {} ", params.getAuthorizationCode());

        AccessToken accessToken = oAuth2LoginService.login(params);
        return response(UserLoginResponse.of(accessToken));
    }
}
