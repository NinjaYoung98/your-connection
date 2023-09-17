package com.sns.yourconnection.model.result.users.admin;

import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.common.UserActivity;
import com.sns.yourconnection.model.entity.users.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserActivityResponse {

    private Long userId;
    private String username;
    private String nickname;
    private UserRole userRole;
    private UserActivity userActivity;

    public static UserActivityResponse fromUser(User user) {
        return new UserActivityResponse(
            user.getId(),
            user.getUsername(),
            user.getNickname(),
            user.getRole(),
            user.getActivity()
        );
    }
}
