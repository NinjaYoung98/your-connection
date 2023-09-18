package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.entity.users.UserRole;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.security.principal.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private UserRole role;

    private UserActivity activity;

    private LocalDateTime createdAt;

    private Map<String, Object> oAuth2Attributes;

    public static User fromEntity(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getNickname(),
            userEntity.getEmail(),
            userEntity.getRole(),
            userEntity.getUserActivity(),
            userEntity.getCreatedAt(),
            Map.of()
        );
    }

    public static User fromPrincipal(UserPrincipal userPrincipal) {
        return new User(
            userPrincipal.getId(),
            userPrincipal.getUsername(),
            userPrincipal.getPassword(),
            userPrincipal.getNickname(),
            userPrincipal.getEmail(),
            userPrincipal.getRole(),
            userPrincipal.getActivity(),
            userPrincipal.getCreatedAt(),
            Map.of()
        );
    }
}
