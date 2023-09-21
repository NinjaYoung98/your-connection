package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.*;

import com.sns.yourconnection.common.annotation.ValidatedPageRequest;
import com.sns.yourconnection.controller.response.PageResponseWrapper;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.param.users.admin.AdminCreateRequest;
import com.sns.yourconnection.model.param.users.admin.PostActivityRequest;
import com.sns.yourconnection.model.param.users.admin.UserActivityRequest;
import com.sns.yourconnection.model.result.users.admin.PostActivityResponse;
import com.sns.yourconnection.model.result.report.PostReportResponse;
import com.sns.yourconnection.model.result.report.UserReportResponse;
import com.sns.yourconnection.model.result.users.admin.UserActivityResponse;
import com.sns.yourconnection.service.users.admin.AdminCreationService;
import com.sns.yourconnection.service.users.admin.ReportActionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminApiController {

    private final AdminCreationService adminCreationService;
    private final ReportActionService reportActionService;

    @PostMapping("create")
    public ResponseSuccess<Void> createAdmin(@RequestBody AdminCreateRequest adminCreateRequest) {

        log.info("[AdminApiController -> Called : createAdmin] "
            + "Create admin account api has called ");

        adminCreationService.createAccount(adminCreateRequest.getEmail());
        return response();
    }

    @GetMapping("/report/users")
    public ResponseSuccess<List<UserActivityResponse>> getUserByActivity(
        @RequestParam UserActivity activity,
        @ValidatedPageRequest Pageable pageable) {

        log.info("[AdminApiController -> Called : getUserByActivity] get user by activity ");

        Page<UserActivityResponse> userActivityPage = reportActionService.getUserByActivity(
                activity, pageable)
            .map(UserActivityResponse::fromUser);

        return response(userActivityPage.getContent(),
            PageResponseWrapper.fromPage(userActivityPage));
    }

    @GetMapping("/report/users/{userId}")
    public ResponseSuccess<List<UserReportResponse>> getUserReportRecord(
        @PathVariable Long userId, @ValidatedPageRequest Pageable pageable) {

        log.info(
            "[AdminApiController -> Called : getUserReportRecord] "
                + "get report record for user : {}", userId);

        Page<UserReportResponse> userReportPage = reportActionService.getUserReportRecord(
                userId, pageable)
            .map(UserReportResponse::fromUserReport);

        return response(userReportPage.getContent(),
            PageResponseWrapper.fromPage(userReportPage));
    }

    @GetMapping("/report/posts")
    public ResponseSuccess<List<PostActivityResponse>> getPostByActivity(
        @RequestParam ContentActivity activity, @ValidatedPageRequest Pageable pageable) {

        log.info("[AdminApiController -> Called : getPostByActivity] get post by activity ");

        Page<PostActivityResponse> postActivityPage = reportActionService.getPostByActivity(
                activity, pageable)
            .map(PostActivityResponse::fromPost);

        return response(postActivityPage.getContent(),
            PageResponseWrapper.fromPage(postActivityPage));
    }

    @GetMapping("/report/posts/{postId}")
    public ResponseSuccess<List<PostReportResponse>> getPostReportRecord(
        @PathVariable Long postId, @ValidatedPageRequest Pageable pageable) {

        log.info("[AdminApiController -> Called : getPostReportRecord] "
            + "get report record for post : {}", postId);

        Page<PostReportResponse> postReportPage = reportActionService.getPostReportRecord(
                postId, pageable)
            .map(PostReportResponse::fromPostReport);

        return response(postReportPage.getContent(),
            PageResponseWrapper.fromPage(postReportPage));
    }

    @PutMapping("/report/users/{userId}")
    public ResponseSuccess<UserActivityResponse> changeUserActivity(
        @PathVariable Long userId, @RequestBody UserActivityRequest userActivityRequest) {

        log.info("[AdminApiController -> Called : changeUserActivity] change user activity  "
            + "user: {} activity : {}", userId, userActivityRequest.getUserActivity().name());

        UserActivityResponse userActivityResponse = UserActivityResponse
            .fromUser(
                reportActionService.changeUserActivity(
                    userActivityRequest.getUserActivity(), userId));

        return response(userActivityResponse);
    }

    @PutMapping("/report/posts/{postId}")
    public ResponseSuccess<PostActivityResponse> changePostActivity(
        @PathVariable Long postId, @RequestBody PostActivityRequest postActivityRequest) {

        log.info("[AdminApiController -> Called : changePostActivity] change post activity  "
            + "post: {} activity : {}", postId, postActivityRequest.getContentActivity().name());

        PostActivityResponse postActivityResponse = PostActivityResponse
            .fromPost(
                reportActionService.changePostActivity(
                    postActivityRequest.getContentActivity(), postId));

        return response(postActivityResponse);
    }
}
