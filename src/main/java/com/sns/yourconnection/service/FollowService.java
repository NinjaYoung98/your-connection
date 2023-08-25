package com.sns.yourconnection.service;

import com.sns.yourconnection.model.follow.dto.Follow;
import com.sns.yourconnection.model.follow.entity.FollowEntity;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.entity.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.FollowRepository;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(Long followingId, User user) {
        /*
         팔로잉 기능
            - follower,following 존재하는지 확인한다.
            - 자기 자신을 팔로잉 할 수 없다.
            - 이미 팔로우 되어 있다면 언팔로우 된다. (언팔로우: 팔로우 취소)
            - 팔로우 중이 아니라면 팔로우 된다.
         */
        UserEntity follower = getUserEntity(user);
        UserEntity following = getUserEntity(followingId);
        validateSelfFollow(follower, following);
        Optional<FollowEntity> follow = followRepository.findByFollowerAndFollowing(follower, following);

        if (!isFollow(follow)) {
            followRepository.save(FollowEntity.of(follower, following));
            log.info("FollowEntity has created with follower: {} following: {}", follower.getId(), following.getId());
        }
    }

    @Transactional(readOnly = true)
    public Page<Follow> getFollowingPage(User user, Pageable pageable) {
        /*
            팔로잉 리스트 조회
                - user 팔로잉 list = 팔로워로 user를 가진 list
         */
        UserEntity userEntity = getUserEntity(user);
        return followRepository.findByFollower(userEntity, pageable).map(Follow::FromEntity);
    }

    @Transactional(readOnly = true)
    public Page<Follow> getFollowerPage(User user, Pageable pageable) {
        /*
            팔로워 리스트 조회
                - user 팔로워 list = 팔로잉으로 user를 가진 list
         */
        UserEntity userEntity = getUserEntity(user);
        return followRepository.findByFollowing(userEntity, pageable).map(Follow::FromEntity);
    }

    private boolean isFollow(Optional<FollowEntity> follow) {
        if (isAlreadyFollow(follow)) {
            deleteFollow(follow);
            return true;
        }
        return false;
    }

    private void deleteFollow(Optional<FollowEntity> follow) {
        FollowEntity followEntity = follow.get();
        log.info("user: {} unfollow user: {}", followEntity.getFollower(), followEntity.getFollower());
        followRepository.delete(followEntity);
    }

    private boolean isAlreadyFollow(Optional<FollowEntity> following) {
        return following.isPresent();
    }

    private void validateSelfFollow(UserEntity follower, UserEntity followee) {
        if (follower.getId() == followee.getId()) {
            throw new AppException(ErrorCode.CANNOT_FOLLOW_YOURSELF);
        }
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public UserEntity getUserEntity(Long followeeId) {
        return userRepository.findById(followeeId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}