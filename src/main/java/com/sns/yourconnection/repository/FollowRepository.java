package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.follow.FollowEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
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


    @Query("SELECT f FROM FollowEntity f WHERE f.followedUser.id = :followedId")
    Page<FollowEntity> findAllByFollowedUserId(@Param("followedId") Long followedId, Pageable pageable);

    @Query("SELECT f FROM FollowEntity f WHERE f.followingUser.id = :followingUserId")
    Page<FollowEntity> findAllByFollowingUserId(@Param("followingUserId") Long followingUserId,
        Pageable pageable);

    @Query("SELECT COUNT(f) FROM FollowEntity f WHERE f.followingUser.id = :userId "
        + "AND f.followedUser.id "
        + "IN (SELECT ff.followingUser.id FROM FollowEntity ff "
        + "WHERE ff.followedUser.id = :targetUserId)")
    Integer countMutualFollows(@Param("userId") Long userId,
        @Param("targetUserId") Long targetUserId);

}
