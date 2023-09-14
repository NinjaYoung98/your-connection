package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.users.common.UserRole;
import com.sns.yourconnection.model.entity.users.UserEntity;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Getter
@AllArgsConstructor
public class User implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private UserRole role;

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
            userEntity.getCreatedAt(),
            Map.of()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.getRole()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
