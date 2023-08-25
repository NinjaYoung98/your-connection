package com.sns.yourconnection.service;


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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional(readOnly = true)
    public Page<Post> getPostPage(Pageable pageable) {
        //  post 조회 기능
        return postRepository.findAll(pageable).map(Post::fromEntity);
    }


    @Transactional
    public Post getPost(Long postId, User user) {
        /*
        특정 post 조회 기능
            -  post 존재하는지 확인
            -  post 에 대한 user 조회 기록 체크
         */
        return postRepository.findById(postId)
            .map(Post::fromEntity)
            .orElseThrow(() -> new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    @Transactional
    public Post updatePost(Long postId, PostRequest postUpdateRequest, User user) {
        /*
        post 를 수정한다(제목, 컨텐츠)
            - post 존재하는지 확인한다.
            - post 작성자만 해당 post 를 수정할 수 있다.
            - post 수정한다.
         */
        PostEntity postEntity = getPostEntity(postId);
        validateMatches(user, postEntity);
        postEntity.update(postUpdateRequest.getTitle(), postUpdateRequest.getContent());
        return Post.fromEntity(postEntity);
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        /*
        post 를 삭제한다.
            - post 존재하는지 확인한다.
            - post 작성자만 해당 post 를 삭제할 수 있다.
            - post 삭제한다.
         */
        PostEntity postEntity = getPostEntity(postId);
        validateMatches(user, postEntity);
        postRepository.delete(postEntity);
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateMatches(User user, PostEntity postEntity) {
        if (postEntity.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.HAS_NOT_PERMISSION_TO_ACCESS);
        }
    }
}
