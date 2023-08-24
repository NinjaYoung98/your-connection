package com.sns.yourconnection.model.user.result;

import com.sns.yourconnection.security.token.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String accessToken;
    private String grantType;
    private Long expiresIn;

    public static UserLoginResponse of(AccessToken accessToken) {
        return new UserLoginResponse(accessToken.getAccessToken(), accessToken.getGrantType(), accessToken.getExpiresIn());
    }
}
