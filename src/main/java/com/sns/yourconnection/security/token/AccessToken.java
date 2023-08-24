package com.sns.yourconnection.security.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    private String accessToken;
    private String grantType;
    private Long expiresIn;

    public static AccessToken of(String accessToken, String grantType, Long expiresIn) {
        return new AccessToken(accessToken, grantType, expiresIn);
    }
}
