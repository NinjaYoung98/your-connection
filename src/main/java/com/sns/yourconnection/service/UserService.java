package com.sns.yourconnection.service;

import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.param.UserJoinRequest;
import com.sns.yourconnection.model.user.param.UserLoginRequest;
import com.sns.yourconnection.model.user.entity.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    public User join(UserJoinRequest userJoinRequest) {
        /*
        회원 정보(username, password, nickname)를 등록한다.
            -  username이 이미 존재할 경우 에러 반환
         */
        DuplicateUsername(userJoinRequest.getUsername());

        UserEntity userEntity = UserEntity.of(
            userJoinRequest.getUsername(), userJoinRequest.getPassword(),
            userJoinRequest.getNickname());
        log.info("UserEntity has created for join with ID: {} username: nickname: {}",
            userEntity.getId(), userEntity.getUsername(), userEntity.getNickname());

        userRepository.save(userEntity);
        return User.fromEntity(userEntity);
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequest userLoginRequest) {
        User user = loadUserByUsername(userLoginRequest.getUsername());
        validatePassword(userLoginRequest, user);
        return "accessToken";
    }

    @Override
    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(User::fromEntity)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void validatePassword(UserLoginRequest userLoginRequest, User user) {
        if (!userLoginRequest.getPassword().equals(user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public void DuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(userEntity -> {
            throw new AppException(ErrorCode.DUPLICATED_USERNAME);
        });
    }
}
