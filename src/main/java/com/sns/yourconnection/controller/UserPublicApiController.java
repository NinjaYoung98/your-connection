package com.sns.yourconnection.controller;

import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.param.UserJoinRequest;
import com.sns.yourconnection.model.user.param.UserLoginRequest;
import com.sns.yourconnection.model.user.result.UserJoinResponse;
import com.sns.yourconnection.model.user.result.UserLoginResponse;
import com.sns.yourconnection.security.token.AccessToken;
import com.sns.yourconnection.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/users")
@Slf4j
public class UserPublicApiController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseSuccess<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        log.info("create a new User Request Details: {}", userJoinRequest);

        User user = userService.join(userJoinRequest);
        log.info("Successfully join for user: {}", user.getId());

        return response(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public ResponseSuccess<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("User is attempting to login with username: {}", userLoginRequest.getUsername());

        AccessToken accessToken = userService.login(userLoginRequest);
        log.info("your connection login is success and issued access token");

        return response(UserLoginResponse.of(accessToken));
    }
}
