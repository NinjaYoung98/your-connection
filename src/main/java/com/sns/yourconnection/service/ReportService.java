package com.sns.yourconnection.service;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import com.sns.yourconnection.model.entity.report.PostReportEntity;
import com.sns.yourconnection.model.entity.report.UserReportEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.model.entity.users.common.UserActivity;
import com.sns.yourconnection.repository.PostReportRepository;
import com.sns.yourconnection.repository.PostRepository;
import com.sns.yourconnection.repository.UserReportRepository;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final int REPORT_LIMIT = 10;
    private final UserReportRepository userReportRepository;
    private final PostReportRepository postReportRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void reportUser(Long reporterId, Long reportedUserId, String text) {

        /*
           유저 신고 기능
              - 유저는 자기 자신을 신고할 수 없습니다.
              - 신고할 유저가 존재하는지 체크합니다.
              - 신고할 유저가 이미 ban 당했는지 체크합니다.
              - 이미 해당 유저를 신고했다면 신고가 접수되지 않습니다.
              - 신고당한 횟수가 REPORT_LIMIT 초과한다면 일반 유저에서 조치 필요 유저로 activity 값이 변경됩니다.
         */

        validateSelfReport(reporterId, reportedUserId);
        UserEntity reportedUserEntity = userRepository.findById(reportedUserId).orElseThrow(
            () -> new AppException(ErrorCode.USER_NOT_FOUND, "Reported user not found"));
        validateUserBanned(reportedUserEntity);

        UserEntity reporterEntity = getReporterEntityById(reporterId);
        if (userReportRepository.existsByReporterAndReportedUser(reporterEntity, reportedUserEntity)) {
            return;
        }
        userReportRepository.save(UserReportEntity.of(reporterEntity, reportedUserEntity, text));

        if (userReportRepository.countByReportedUser(reportedUserEntity) > REPORT_LIMIT) {
            reportedUserEntity.changeActivity(UserActivity.ACTION_REQUIRED);
        }
    }

    @Transactional
    public void reportPost(Long reporterId, Long reportedPostId, String text) {

         /*
            게시물 신고 기능
               - 신고할 컨텐츠가 존재하는지 체크합니다.
               - 본인이 작성한 게시물을 신고할 수 없습니다.
               - 신고할 컨첸츠가 이미 제한되었는지 체크합니다.
               - 이미 해당 컨텐츠를 신고했다면 신고가 접수되지 않습니다.
               - 신고당한 횟수가 REPORT_LIMIT 초과한다면 일반 컨텐츠에서 선정적인 컨첸츠로 분류됩니다.
          */

        PostEntity reportedPostEntity = postRepository.findById(reportedPostId).orElseThrow(
            () -> new AppException(ErrorCode.POST_DOES_NOT_EXIST, "Reported post not found"));
        validateSelfReport(reporterId, reportedPostEntity.getUser().getId());
        validatedRestrictedContent(reportedPostEntity);

        UserEntity reporterEntity = getReporterEntityById(reporterId);
        if (postReportRepository.existsByReporterAndReportedPost(reporterEntity, reportedPostEntity)) {
            return;
        }
        postReportRepository.save(PostReportEntity.of(reporterEntity, reportedPostEntity, text));

        if (postReportRepository.countByReportedPost(reportedPostEntity) > REPORT_LIMIT) {
            reportedPostEntity.changeActivity(ContentActivity.ACTION_REQUIRED);
        }
    }

    private UserEntity getReporterEntityById(Long reporterId) {
        return userRepository.findById(reporterId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUserBanned(UserEntity reportedUserEntity) {
        if (reportedUserEntity.getUserActivity() == UserActivity.BAN) {
            throw new AppException(ErrorCode.USER_BANNED);
        }
    }

    private void validateSelfReport(Long reporterId, Long reportedUId) {
        if (reporterId == reportedUId) {
            throw new AppException(ErrorCode.CANNOT_REPORT_YOURSELF);
        }
    }

    private void validatedRestrictedContent(PostEntity reportedPostEntity) {
        if (reportedPostEntity.getContentActivity() == ContentActivity.RESTRICTED) {
            throw new AppException(ErrorCode.RESTRICTED_CONTENT);
        }
    }
}
