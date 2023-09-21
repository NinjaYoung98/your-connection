package com.sns.yourconnection.service.users.admin;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.dto.Post;
import com.sns.yourconnection.model.dto.PostReport;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.dto.UserReport;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.repository.PostReportRepository;
import com.sns.yourconnection.repository.PostRepository;
import com.sns.yourconnection.repository.UserReportRepository;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 신고 내역과 userActivity 값을 admin 권한으로 관리할 수 있는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ReportActionService {

    private final UserReportRepository userReportRepository;
    private final UserRepository userRepository;
    private final PostReportRepository postReportRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<User> getUserByActivity(UserActivity userActivity, Pageable pageable) {
        // userActivity 기준으로 user 조회
        return userRepository.findByUserActivity(userActivity, pageable)
            .map(User::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<UserReport> getUserReportRecord(Long userId, Pageable pageable) {
        // user 신고 내역 조회
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userReportRepository.findByReportedUser(userEntity, pageable)
            .map(UserReport::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostByActivity(ContentActivity contentActivity, Pageable pageable) {
        // contentActivity 값으로 post 조회
        return postRepository.findByContentActivity(contentActivity, pageable)
            .map(Post::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostReport> getPostReportRecord(Long postId, Pageable pageable) {
        // post 신고 내역 조회
        PostEntity postEntity = postRepository.findById(postId)
            .orElseThrow(
                () -> new AppException(ErrorCode.POST_DOES_NOT_EXIST));
        return postReportRepository.findByReportedPost(postEntity, pageable)
            .map(PostReport::fromEntity);
    }

    @Transactional
    public User changeUserActivity(UserActivity userActivity, Long userId) {
        // userActivity 값 변경
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        userEntity.changeActivity(userActivity);
        return User.fromEntity(userEntity);
    }

    @Transactional
    public Post changePostActivity(ContentActivity contentActivity, Long postId) {
        // post contentActivity 값 변경
        PostEntity postEntity = postRepository.findById(postId)
            .orElseThrow(
                () -> new AppException(ErrorCode.POST_DOES_NOT_EXIST));
        postEntity.changeActivity(contentActivity);
        return Post.fromEntity(postEntity);
    }
}
