package com.sns.yourconnection.service.post;

import com.sns.yourconnection.model.dto.Comment;
import com.sns.yourconnection.model.param.comment.CommentRequest;
import com.sns.yourconnection.model.entity.comment.CommentEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.CommentRepository;
import com.sns.yourconnection.repository.PostRepository;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Comment createComment(Long postId, CommentRequest commentRequest, User user) {
        // 해당 포스트가 존재한다면 comment를 작성할 수 있다.
        UserEntity userEntity = getUserEntity(user);
        PostEntity postEntity = getPostEntity(postId);
        CommentEntity commentEntity = CommentEntity.of(commentRequest.getComment(), userEntity,
            postEntity);

        log.info("CommentEntity for Post: {}  has created with ID: {} comment: {} by user: {}"
            , postEntity.getId(), commentEntity.getId(), commentEntity.getComment(),
            userEntity.getId());
        commentRepository.save(commentEntity);
        return Comment.fromEntity(commentEntity);
    }

    @Transactional(readOnly = true)
    public Page<Comment> getCommentPage(Long postId, Pageable pageable) {
        /*
        post에 달린 comment 목록을 확인한다.
            - post가 존재할 경우에 comment 목록을 확인할 수 있다.
         */

        PostEntity postEntity = getPostEntity(postId);
        return commentRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    @Transactional
    public Comment updateComment(Long postId, Long commentId, CommentRequest commentRequest,
        User user) {
        /*
        comment 를 수정한다
            - comment 존재하는지 확인한다.
            - post, comment 가 서로 match 하는지 확인한다.
            - comment 작성자만 해당 comment 를 수정할 수 있다.
            - comment 변경사항이 없을 경우 comment log 객체를 생성하지 않는다.
            - comment 수정한다.
            - comment 수정 내역을 comment log에 기록한다.
         */
        CommentEntity commentEntity = getCommentEntity(commentId);
        validateMatchesPost(postId, commentEntity);
        validateAuthorization(user, commentEntity);
        commentEntity.update(commentRequest.getComment());
        return Comment.fromEntity(commentEntity);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, User user) {
        /*
        comment 를 삭제한다.
            - comment 남겨진 post 존재하는지 확인한다.
            - comment 작성자만 해당 comment 를 삭제할 수 있다.
            - comment 삭제한다.
         */
        CommentEntity commentEntity = getCommentEntity(commentId);
        validateMatchesPost(postId, commentEntity);
        validateAuthorization(user, commentEntity);
        commentRepository.delete(commentEntity);
    }


    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->
            new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    public CommentEntity getCommentEntity(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
            new AppException(ErrorCode.COMMENT_DOES_NOT_EXIST));
    }

    public void validateAuthorization(User user, CommentEntity commentEntity) {
        if (user.getId() != commentEntity.getUser().getId()) {
            throw new AppException(ErrorCode.HAS_NOT_PERMISSION_TO_ACCESS);
        }
    }

    public void validateMatchesPost(Long postId, CommentEntity commentEntity) {
        if (commentEntity.getPost().getId() != postId) {
            throw new AppException(ErrorCode.NONE_MATCH);
        }
    }
}
