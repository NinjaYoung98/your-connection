package com.sns.yourconnection.model.result.follow;

import com.sns.yourconnection.model.dto.Follow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {
    private Long id;
    private String nickName;

    public static FollowerResponse fromFollow(Follow follow) {
        return new FollowerResponse(
                follow.getFollower().getId(),
                follow.getFollower().getNickname());
    }
}
