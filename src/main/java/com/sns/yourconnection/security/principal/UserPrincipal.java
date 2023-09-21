package com.sns.yourconnection.security.principal;

import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.users.EmailVerified;
import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.model.entity.users.UserRole;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails, OAuth2User {

    private static final long serialVersionUID = -2082957148955168665L;

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private EmailVerified emailVerified;

    private UserRole role;

    private UserActivity activity;

    private LocalDateTime createdAt;

    private Map<String, Object> oAuth2Attributes;


    public static UserDetails fromUser(User user) {
        return new UserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            user.getNickname(),
            user.getEmail(),
            user.getEmailVerified(),
            user.getRole(),
            user.getActivity(),
            user.getCreatedAt(),
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
        if (activity == UserActivity.BAN) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }
}
