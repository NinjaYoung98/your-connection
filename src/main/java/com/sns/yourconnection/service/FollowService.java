package com.sns.yourconnection.service;

import com.sns.yourconnection.model.dto.Follow;
import com.sns.yourconnection.model.entity.follow.FollowEntity;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.result.follow.UserRelatedFollowingResponse;
import com.sns.yourconnection.repository.FollowRepository;
import com.sns.yourconnection.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
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
            - followingUser,followedUser 존재하는지 확인한다.
            - 자기 자신을 팔로잉 할 수 없다.
            - 이미 팔로우 되어 있다면 언팔로우 된다. (언팔로우: 팔로우 취소)
            - 팔로우 중이 아니라면 팔로우 된다.
         */
        UserEntity followingUser = getUserEntity(user.getId());
        UserEntity followedUser = getUserEntity(followingId);
        validateSelfFollow(followingUser, followedUser);
        Optional<FollowEntity> follow = followRepository.findByFollowingUserAndFollowedUser(
            followingUser, followedUser);

        if (!isFollowRelation(follow)) {
            followRepository.save(FollowEntity.of(followingUser, followedUser));
            log.info("FollowEntity has created with followingUser: {} followedUser: {}",
                followingUser.getId(),
                followedUser.getId());
        }
    }


    @Transactional(readOnly = true)
    public Page<Follow> getFollowingPage(User user, Pageable pageable) {
        //   팔로잉 리스트 조회
        return followRepository.findAllByFollowingUserId(user.getId(), pageable)
            .map(Follow::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<Follow> getFollowerPage(User user, Pageable pageable) {
        //  팔로워 리스트 조회
        return followRepository.findAllByFollowedUserId(user.getId(), pageable)
            .map(Follow::fromEntity);
    }

    @Transactional(readOnly = true)
    public UserRelatedFollowingResponse getUserRelatedToFriend(User user, Long targetId,
        Pageable pageable) {
        /*
            - 함께 아는 친구 기능
                - 특정 유저 검색시 그 유저를 팔로잉하고 있는 나의 팔로잉 리스트를 조회합니다.
                - 네이밍: 조회된 user 사이의 관계를 표현
         */
        Integer relatedUserTotalCount = followRepository.countMutualFollows(user.getId(), targetId);
        return UserRelatedFollowingResponse.fromFollowAndTotalCount(
            getUserRelatedToFollowingList(user, targetId, pageable), relatedUserTotalCount);
    }


    private boolean isFollowRelation(Optional<FollowEntity> follow) {
        if (follow.isPresent()) {
            deleteFollow(follow);
            return true;
        }
        return false;
    }

    private void deleteFollow(Optional<FollowEntity> follow) {
        FollowEntity followEntity = follow.get();
        log.info("user: {} unfollow user: {}", followEntity.getFollowingUser(),
            followEntity.getFollowingUser());
        followRepository.delete(followEntity);
    }

    private List<Follow> getUserRelatedToFollowingList(User user, Long targetId,
        Pageable pageable) {

        return followRepository.findAllByFollowingUserId(user.getId(), pageable)
            .stream()
            .flatMap(relationAsFollowingUser -> relationAsFollowingUser.getFollowedUser()
                .getFollowingList().stream())
            .filter(relationFromUserFollowing -> relationFromUserFollowing.getFollowedUser().getId()
                == targetId).map(Follow::fromEntity)
            .collect(Collectors.toList());
    }

    private void validateSelfFollow(UserEntity followingUser, UserEntity followedUser) {
        if (followingUser.getId() == followedUser.getId()) {
            throw new AppException(ErrorCode.CANNOT_FOLLOW_YOURSELF);
        }
    }

    public UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}