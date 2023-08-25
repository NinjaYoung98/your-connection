package com.sns.yourconnection.model.follow.entity;

import com.sns.yourconnection.model.audit.AuditEntity;
import com.sns.yourconnection.model.user.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow")
public class FollowEntity extends AuditEntity {

    /*
        follower = 팔로우를 신청한 user
        following = 팔로우를 당한 user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower")
    private UserEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following")
    private UserEntity following;

    private FollowEntity(UserEntity follower, UserEntity following) {
        this.follower = follower;
        this.following = following;
    }

    public static FollowEntity of(UserEntity follower, UserEntity following) {
        return new FollowEntity(follower, following);
    }
}
