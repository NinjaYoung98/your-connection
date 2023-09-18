package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostLikeEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    Optional<PostLikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    Integer countByPost(PostEntity post);

}
