package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.*;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.param.report.PostReportRequest;
import com.sns.yourconnection.model.param.report.UserReportRequest;
import com.sns.yourconnection.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
public class ReportApiController {

    private final ReportService reportService;

    @PostMapping("user/{reportedUserId}")
    public ResponseSuccess<Void> repostUser(@RequestBody UserReportRequest userReportRequest,
        @AuthUser User user, @PathVariable Long reportedUserId) {

        log.info("[ReportApiController -> called : reportUser] user : {} reportedUser : {}  ",
            user.getId(), reportedUserId);
        reportService.reportUser(user.getId(), reportedUserId, userReportRequest.getText());

        log.info("[ReportApiController -> completed : reportUser]");
        return response();
    }

    @PostMapping("post/{reportedPostId}")
    public ResponseSuccess<Void> reportPost(@RequestBody PostReportRequest postReportRequest,
        @AuthUser User user, @PathVariable Long reportedPostId) {

        log.info("[ReportApiController -> called : reportPost] user : {} reportedPost : {}  ",
            user.getId(), reportedPostId);
        reportService.reportPost(user.getId(), reportedPostId, postReportRequest.getText());

        log.info("[ReportApiController -> completed : reportPost]");
        return response();
    }
}
