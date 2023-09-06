package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.follow.FollowEntity;
import com.sns.yourconnection.model.entity.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findByFollowingUserAndFollowedUser(UserEntity followingUser,
        UserEntity followedUser);


    @Query("SELECT f FROM FollowEntity f WHERE f.followedUser.Id = :followedId")
    Page<FollowEntity> findByFollowedUserId(@Param("followedId") Long followedId, Pageable pageable);

    @Query("SELECT f FROM FollowEntity f WHERE f.followingUser.Id = :followingUserId")
    Page<FollowEntity> findByFollowingUserId(@Param("followingUserId") Long followingUserId,
        Pageable pageable);

    @Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.followingUser.Id = :userId "
        + "AND f.followedUser.Id "
        + "IN (SELECT ff.followingUser.Id FROM FollowEntity ff "
        + "WHERE ff.followedUser.Id = :targetUserId)")
    Integer countMutualFollows(@Param("userId") Long userId,
        @Param("targetUserId") Long targetUserId);

}
