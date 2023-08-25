package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.like.entity.LikeCountEntity;
import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LikeCountRepository extends JpaRepository<LikeCountEntity, Long> {

    Optional<LikeCountEntity> findByUserAndPost(UserEntity user, PostEntity post);

    Integer countByPost(PostEntity post);
}
