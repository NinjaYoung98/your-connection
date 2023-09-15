package com.sns.yourconnection.model.param.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String username;
    private String password;
    private String nickname;
    private String email;
}
