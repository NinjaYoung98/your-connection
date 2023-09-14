package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.like.LikeCountEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeCountRepository extends JpaRepository<LikeCountEntity, Long> {

    Optional<LikeCountEntity> findByUserAndPost(UserEntity user, PostEntity post);

    Integer countByPost(PostEntity post);
}
