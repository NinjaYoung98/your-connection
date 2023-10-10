package com.sns.yourconnection.service.users;

import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.EmailVerified;
import com.sns.yourconnection.model.entity.users.UserProfileImageEntity;
import com.sns.yourconnection.model.param.users.UserModifyPasswordRequest;
import com.sns.yourconnection.model.param.users.UserModifyUsernameRequest;
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

    @Transactional
    public User join(UserJoinRequest userJoinRequest) {
        /*
        회원 정보(username, password, nickname, email)를 등록한다.
            -  username이 이미 존재할 경우 에러 반환
            -  중복 이메일 허용 x
         */
        duplicateUsername(userJoinRequest.getUsername());
        duplicateUserEmail(userJoinRequest.getEmail());

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
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        User user = User.fromEntity(userEntity);

        validateEmailVerify(userEntity);
        validatePassword(userLoginRequest, user);
        return jwtTokenGenerator.generateAccessToken(user.getUsername());
    }

    @Transactional
    public void modifyPassword(UserModifyPasswordRequest modifyPasswordRequest, User user) {
        /*
        패스워드 변경 기능
            - 기존의 패스워드와 동일하게 변경 x
         */

        validateSamePassword(modifyPasswordRequest, user);

        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        userEntity.modifyPassword(
            encoder.encode(modifyPasswordRequest.getSavePassword()));
    }

    @Transactional
    public void modifyUsername(UserModifyUsernameRequest userModifyUsernameRequest, User user) {
        /*
        username 변경 기능
            - 중복된 username 허용 x
         */

        duplicateUsername(userModifyUsernameRequest.getSaveUsername());

        UserEntity userEntity = userRepository.findByUsername(user.getUsername())
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        userEntity.modifyUsername(userModifyUsernameRequest.getSaveUsername());
    }

    private static void validateEmailVerify(UserEntity userEntity) {
        if (userEntity.getEmailVerified() == EmailVerified.UNVERIFIED) {
            throw new AppException(ErrorCode.HAS_NOT_AUTHENTICATION, "이메일 인증을 부탁드립니다.");
        }
    }

    private void validateSamePassword(UserModifyPasswordRequest modifyPasswordRequest, User user) {
        if (encoder.matches(modifyPasswordRequest.getSavePassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "기존 패스워드와 동일합니다.");
        }
    }

    private void validatePassword(UserLoginRequest userLoginRequest, User user) {
        if (!encoder.matches(userLoginRequest.getPassword(), user.getPassword())) {

            countLoginFailed(user);

            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private void countLoginFailed(User user) {
        boolean shouldLockAccount = loginFailedRepository.checkLockAccountByKey(user.getUsername());

        if (shouldLockAccount) {
            accountLockService.lockUserAccount(user);
        }
    }


    @Transactional
    public FileInfo uploadProfile(User user, MultipartFile userProfileImage) {

        if (userProfileImage == null || userProfileImage.isEmpty()) {
            return FileInfo.EMPTY;
        }

        UserEntity userEntity = userRepository.findById(user.getId())
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

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

    private void duplicateUsername(String username) {
        userRepository.findByUsername(username).ifPresent(
            userEntity -> {
                throw new AppException(ErrorCode.DUPLICATED_USERNAME);
            });
    }

    private void duplicateUserEmail(String email) {
        userRepository.findByEmail(email).ifPresent(
            userEntity -> {
                throw new AppException(ErrorCode.DUPLICATED_EMAIL);
            });
    }
}

