package com.sns.yourconnection.model.follow.result;

import com.sns.yourconnection.model.follow.dto.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowingResponse {
    private Long id;
    private String nickName;

    public static FollowingResponse fromFollow(Follow follow) {
        return new FollowingResponse(
                follow.getFollowing().getId(),
                follow.getFollowing().getNickname());
    }
}
