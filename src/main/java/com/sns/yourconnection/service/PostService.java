package com.sns.yourconnection.service;


import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.model.post.dto.Post;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.repository.*;
import com.sns.yourconnection.model.post.param.PostRequest;

import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.entity.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Post createPost(PostRequest postCreateRequest, User user) {
        // post 작성 기능
        UserEntity userEntity = getUserEntity(user);
        PostEntity postEntity = PostEntity.of(postCreateRequest.getTitle(),
            postCreateRequest.getContent(), userEntity);
        log.info("PostEntity has created for create a new post with ID: {} title: {} content: {}"
            , postEntity.getId(), postEntity.getTitle(), postEntity.getContent());
        postRepository.save(postEntity);
        return Post.fromEntity(postEntity);
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->
            new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
