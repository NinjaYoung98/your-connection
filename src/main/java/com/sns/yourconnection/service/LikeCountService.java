package com.sns.yourconnection.service;

import com.sns.yourconnection.model.like.entity.LikeCountEntity;
import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.model.user.entity.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.LikeCountRepository;
import com.sns.yourconnection.repository.PostRepository;
import com.sns.yourconnection.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeCountService {

    private final LikeCountRepository likeCountRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /*
        좋아요 기능 개발
            - likeCountRepository에 UserEntity, PostEntit에 데이터가 있으면 delete(좋아요 1 감소), 없으면 save (좋아요 1 증가)
     */
    @Transactional(readOnly = true)
    public Integer getLikeCount(Long postId) {
        PostEntity postEntity = getPostEntity(postId);
        return likeCountRepository.countByPost(postEntity);
    }

    @Transactional
    public void setLikeCount(Long postId, User user) {
        UserEntity userEntity = getUserEntity(user);
        PostEntity postEntity = getPostEntity(postId);
        Optional<LikeCountEntity> likeCountEntityOptional = getLikeCountEntityOptional(userEntity,
            postEntity);
        if (!isLiked(likeCountEntityOptional)) {
            increaseLikeCount(userEntity, postEntity);
        }
    }

    private boolean isLiked(Optional<LikeCountEntity> likeCountEntityOptional) {
        if (likeCountEntityOptional.isPresent()) {
            removeLike(likeCountEntityOptional);
            return true;
        }
        return false;
    }

    private void removeLike(Optional<LikeCountEntity> likeCountEntityOptional) {
        LikeCountEntity likeCountEntity = likeCountEntityOptional.get();

        log.info("Decrease like count for post: {} by user; {}",
            likeCountEntity.getPost().getId(), likeCountEntity.getUser().getId());

        likeCountRepository.delete(likeCountEntity);
    }

    private void increaseLikeCount(UserEntity userEntity, PostEntity postEntity) {
        LikeCountEntity likeCountEntity = LikeCountEntity.of(userEntity, postEntity);
        likeCountRepository.save(likeCountEntity);
        log.info("Increase like count for post: {} by user; {}", postEntity.getId(),
            userEntity.getId());
    }

    public Optional<LikeCountEntity> getLikeCountEntityOptional(UserEntity userEntity,
        PostEntity postEntity) {
        return likeCountRepository.findByUserAndPost(userEntity, postEntity);
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() ->
            new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
