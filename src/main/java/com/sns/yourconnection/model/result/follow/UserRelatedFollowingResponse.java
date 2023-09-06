package com.sns.yourconnection.model.result.follow;

import com.sns.yourconnection.model.dto.Follow;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRelatedFollowingResponse {

    List<FollowerResponse> followList;
    Integer relatedUserTotalCount;

    public static UserRelatedFollowingResponse fromFollowAndTotalCount(List<Follow> followList,
        Integer relatedUserTotalCount) {
        return new UserRelatedFollowingResponse(
            toFollowerResponse(followList), relatedUserTotalCount);
    }

    private static List<FollowerResponse> toFollowerResponse(List<Follow> followList) {
        return followList.stream().map(FollowerResponse::fromFollow).collect(
            Collectors.toList());
    }
}
