package com.sns.yourconnection.model.user.result;

import com.sns.yourconnection.model.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJoinResponse {

    private String userName;
    private String nickName;
    private LocalDateTime createdAt;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
            user.getUsername(),
            user.getNickname(),
            user.getCreatedAt());
    }
}
