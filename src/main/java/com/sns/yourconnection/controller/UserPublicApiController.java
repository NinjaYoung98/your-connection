package com.sns.yourconnection.controller;

import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.param.UserJoinRequest;
import com.sns.yourconnection.model.user.param.UserLoginRequest;
import com.sns.yourconnection.model.user.result.UserJoinResponse;
import com.sns.yourconnection.model.user.result.UserLoginResponse;
import com.sns.yourconnection.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/users")
@Slf4j
public class UserPublicApiController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        log.info("create a new User Request Details: {}", userJoinRequest);

        User user = userService.join(userJoinRequest);
        log.info("Successfully join for user: {}", user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        log.info("User is attempting to login with username: {}", userLoginRequest.getUsername());

        String accessToken = userService.login(userLoginRequest);
        log.info("your connection login is success and issued access token");

        return ResponseEntity.status(HttpStatus.OK).body(UserLoginResponse.of(accessToken));
    }
}
