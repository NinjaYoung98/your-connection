package com.sns.yourconnection.service.users;

import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserProfileImageEntity;
import com.sns.yourconnection.repository.redis.LoginFailedRepository;
import com.sns.yourconnection.utils.files.FileInfo;
import com.sns.yourconnection.model.param.users.UserJoinRequest;
import com.sns.yourconnection.model.param.users.UserLoginRequest;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.UserRepository;
import com.sns.yourconnection.security.token.AccessToken;
import com.sns.yourconnection.security.token.JwtTokenGenerator;
import com.sns.yourconnection.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final LoginFailedRepository loginFailedRepository;
    private final BCryptPasswordEncoder encoder;
    private final StorageService storageService;
    private final AccountLockService accountLockService;
    private final JwtTokenGenerator jwtTokenGenerator;
    private static final Integer INIT_LOGIN_TRIAL_COUNT = 0;
    private static final Integer MAX_ATTEMPT_COUNT = 10;

    @Transactional
    public User join(UserJoinRequest userJoinRequest) {
        /*
        회원 정보(username, password, nickname, email)를 등록한다.
            -  username이 이미 존재할 경우 에러 반환
            -  중복 이메일 허용 x
         */
        DuplicateUsername(userJoinRequest.getUsername());
        DuplicateUserEmail(userJoinRequest.getEmail());

        UserEntity userEntity = UserEntity.of(
            userJoinRequest.getUsername(), encoder.encode(userJoinRequest.getPassword()),
            userJoinRequest.getNickname(), userJoinRequest.getEmail());

        userRepository.save(userEntity);
        log.info("UserEntity has created for join with ID: {} username:{} nickname: {} email : {}",
            userEntity.getId(), userEntity.getUsername(), userEntity.getNickname(),
            userEntity.getEmail());

        return User.fromEntity(userEntity);
    }


    @Transactional
    public AccessToken login(UserLoginRequest userLoginRequest) {
        /*
        로그인 기능
            - username 등록되어 있지 않다면 에러 반환
            - 이메일 인증이 되지 않았다면 로그인 불가능
            - username 이 password 와 일치하지 않는다면 에러 반환
            - 로그인 5회이상 실패시 계정 잠금 (이메일 인증 필요)
         */

        UserEntity userEntity = userRepository.findByUsername(userLoginRequest.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = User.fromEntity(userEntity);

        ValidatePassword(userLoginRequest, user);
        return jwtTokenGenerator.generateAccessToken(user.getUsername());
    }

    private void ValidatePassword(UserLoginRequest userLoginRequest, User user) {
        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            countLoginFailed(user);

            throw new AppException(ErrorCode.HAS_NOT_AUTHENTICATION,
                "Your account requires email verification.");
        }
    }

    public void countLoginFailed(User user) {
        Long attemptCount = incrementFailedCount(user);

        log.info("attemptCount : {}", attemptCount);

        if (attemptCount >= MAX_ATTEMPT_COUNT) {
            accountLockService.lockUserAccount(user);
            loginFailedRepository.delete(user.getUsername()); // 계정 잠금 후 실패 횟수 초기화
        }
    }

    private Long incrementFailedCount(User user) {
        if (loginFailedRepository.getValues(user.getUsername()) == null) {
            loginFailedRepository.setValue(user.getUsername(), INIT_LOGIN_TRIAL_COUNT);
        }
        return loginFailedRepository.increment(user.getUsername());
    }


    @Transactional
    public FileInfo uploadProfile(User user, MultipartFile userProfileImage) {

        if (userProfileImage == null || userProfileImage.isEmpty()) {
            return FileInfo.EMPTY;
        }

        UserEntity userEntity = userRepository.findById(user.getId())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getProfileImage() != null) {
            storageService.delete(userEntity.getProfileImage().getStoreFilename());
        }

        FileInfo fileInfo = storageService.upload(userProfileImage);

        userEntity.uploadProfileImage(
            UserProfileImageEntity.of(
                fileInfo.getOriginalFilename(),
                fileInfo.getStoreFilename(),
                fileInfo.getPathUrl(), userEntity));
        return fileInfo;
    }

    @Transactional
    public void deleteProfile(User user) {

        UserEntity userEntity = userRepository.findById(user.getId())
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (userEntity.getProfileImage() != null) {
            userEntity.removeProfileImage();
            storageService.delete(userEntity.getProfileImage().getStoreFilename());
        }
    }

    private void DuplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(
            userEntity -> {
                throw new AppException(ErrorCode.DUPLICATED_USERNAME);
            });
    }

    private void DuplicateUserEmail(String email) {
        userRepository.findByEmail(email).ifPresent(
            userEntity -> {
                throw new AppException(ErrorCode.DUPLICATED_EMAIL);
            });
    }
}

