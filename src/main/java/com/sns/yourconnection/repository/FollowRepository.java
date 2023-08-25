package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.follow.entity.FollowEntity;
import com.sns.yourconnection.model.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByFollowerAndFollowing(UserEntity follower, UserEntity following);

    Page<FollowEntity> findByFollower(UserEntity follower, Pageable pageable);

    Page<FollowEntity> findByFollowing(UserEntity following, Pageable pageable);
}
