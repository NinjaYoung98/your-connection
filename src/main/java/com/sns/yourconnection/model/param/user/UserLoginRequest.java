package com.sns.yourconnection.model.param.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserLoginRequest {

    private String username;
    private String password;
}