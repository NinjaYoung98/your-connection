package com.sns.yourconnection.model.entity.follow;

import com.sns.yourconnection.model.entity.common.audit.AuditEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
public class FollowEntity extends AuditEntity {

    /*
        followingUser = 팔로우를 신청한 user
        followedUser = 팔로우를 당한 user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user_id")
    private UserEntity followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user_id")
    private UserEntity followedUser;

    private FollowEntity(UserEntity followingUser, UserEntity followedUser) {
        this.followingUser = followingUser;
        this.followedUser = followedUser;
        this.followingUser.getFollowingList().add(this);
        this.followedUser.getFollowedList().add(this);
    }

    public static FollowEntity of(UserEntity followingUser, UserEntity followedUser) {
        return new FollowEntity(followingUser, followedUser);
    }
}
