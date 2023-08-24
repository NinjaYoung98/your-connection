package com.sns.yourconnection.model.user.dto;

import com.sns.yourconnection.model.user.entity.UserRole;
import com.sns.yourconnection.model.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;


@Getter
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private UserRole role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Map<String, Object> oAuth2Attributes;


    public static User fromEntity(UserEntity userEntity) {
        return new User(
            userEntity.getId(),
            userEntity.getUsername(),
            userEntity.getPassword(),
            userEntity.getNickname(),
            userEntity.getRole(),
            userEntity.getCreatedAt(),
            userEntity.getUpdatedAt(),
            Map.of()
        );
    }
}
