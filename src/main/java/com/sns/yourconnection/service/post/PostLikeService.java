package com.sns.yourconnection.service.post;

import com.sns.yourconnection.model.entity.post.PostLikeEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.repository.PostLikeRepository;
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
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /*
        좋아요 기능 개발
            - likeCountRepository에 UserEntity, PostEntit에 데이터가 있으면 delete(좋아요 1 감소), 없으면 save (좋아요 1 증가)
     */
    @Transactional(readOnly = true)
    public Integer getLikeCount(Long postId) {
        PostEntity postEntity = getPostEntity(postId);
        return postLikeRepository.countByPost(postEntity);
    }

    @Transactional
    public void setLikeCount(Long postId, User user) {
        UserEntity userEntity = getUserEntity(user);
        PostEntity postEntity = getPostEntity(postId);

        Optional<PostLikeEntity> likeCountEntityOptional = postLikeRepository.findByUserAndPost(
            userEntity, postEntity);

        if (!isLiked(likeCountEntityOptional)) {
            increaseLikeCount(userEntity, postEntity);
        }
    }

    private boolean isLiked(Optional<PostLikeEntity> likeCountEntityOptional) {
        if (likeCountEntityOptional.isPresent()) {
            removeLike(likeCountEntityOptional);
            return true;
        }
        return false;
    }

    private void removeLike(Optional<PostLikeEntity> likeCountEntityOptional) {
        PostLikeEntity postLikeEntity = likeCountEntityOptional.get();

        log.info("Decrease like count for post: {} by user; {}",
            postLikeEntity.getPost().getId(), postLikeEntity.getUser().getId());

        postLikeRepository.delete(postLikeEntity);

    }

    private void increaseLikeCount(UserEntity userEntity, PostEntity postEntity) {
        PostLikeEntity postLikeEntity = PostLikeEntity.of(userEntity, postEntity);
        postLikeRepository.save(postLikeEntity);
        log.info("Increase like count for post: {} by user; {}", postEntity.getId(),
            userEntity.getId());
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(
                () -> new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
