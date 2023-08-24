package com.sns.yourconnection.model.user.param;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
public class UserJoinRequest {

    private String username;
    private String password;
    private String nickname;
}
