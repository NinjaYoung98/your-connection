package com.sns.yourconnection.model.user.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserLoginResponse {

    private String accessToken;


    public static UserLoginResponse of(String accessToken) {
        return new UserLoginResponse(accessToken);
    }
}
