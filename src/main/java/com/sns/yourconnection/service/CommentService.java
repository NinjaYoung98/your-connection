package com.sns.yourconnection.service;

import com.sns.yourconnection.model.comment.dto.Comment;
import com.sns.yourconnection.model.comment.param.CommentRequest;
import com.sns.yourconnection.model.comment.entity.CommentEntity;
import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.entity.UserEntity;
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

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->
            new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }
}
