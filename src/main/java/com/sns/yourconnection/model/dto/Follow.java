package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.follow.FollowEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    private Long id;
    private User follower;
    private User following;

    public static Follow FromEntity(FollowEntity followEntity) {
        return new Follow(followEntity.getId(),
                User.fromEntity(followEntity.getFollower()),
                User.fromEntity(followEntity.getFollowing()));
    }
}
