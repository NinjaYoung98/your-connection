package com.sns.yourconnection.controller;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.common.annotation.ValidatedPageRequest;
import com.sns.yourconnection.model.result.follow.FollowerResponse;
import com.sns.yourconnection.model.result.follow.FollowingResponse;
import com.sns.yourconnection.controller.response.PageResponseWrapper;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sns.yourconnection.controller.response.ResponseSuccess.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApiController {

    private final FollowService followService;

    @PostMapping("/follow/{followingId}")
    public ResponseSuccess follow(@PathVariable Long followingId, @AuthUser User user) {
        log.info("User: {} is attempting to follow user: {}", user, followingId);

        followService.follow(followingId, user);
        log.info("User: {} has successfully followed user: {}", user, followingId);

        return response();
    }

    @GetMapping("/follow/following")
    public ResponseSuccess<List<FollowingResponse>> getFollowingPage(@AuthUser User user,
        @ValidatedPageRequest Pageable pageable) {
        log.info("Getting following list for user: {} and Pageable Details: {}", user.getId(),
            pageable);

        Page<FollowingResponse> followingPage = followService.getFollowingPage(user, pageable)
            .map(FollowingResponse::fromFollow);
        log.info("Retrieved following list successfully. Current page size: {}",
            followingPage.getSize());

        return response(followingPage.getContent(), PageResponseWrapper.fromPage(followingPage));
    }

    @GetMapping("/follow/followers")
    public ResponseSuccess<List<FollowerResponse>> getFollowerPage(@AuthUser User user,
        @ValidatedPageRequest Pageable pageable) {
        log.info("Getting follower list for user: {} and Pageable Details: {}", user.getId(),
            pageable);

        Page<FollowerResponse> followerPage = followService.getFollowerPage(user, pageable)
            .map(FollowerResponse::fromFollow);
        log.info("Retrieved follower list successfully. Current page size: {}",
            followerPage.getSize());

        return response(followerPage.getContent(), PageResponseWrapper.fromPage(followerPage));
    }
}
