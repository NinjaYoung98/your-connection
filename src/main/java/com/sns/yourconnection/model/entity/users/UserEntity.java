package com.sns.yourconnection.model.entity.users;

import com.sns.yourconnection.model.entity.follow.FollowEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.users.common.UserActivity;
import com.sns.yourconnection.model.entity.users.common.UserRole;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String username;

    private String password;

    private String nickname;

    private String email;

    @OneToMany(mappedBy = "followingUser", fetch = FetchType.LAZY)
    private List<FollowEntity> followingList = new ArrayList<>();

    @OneToMany(mappedBy = "followedUser", fetch = FetchType.LAZY)
    private List<FollowEntity> followedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "profile_image_id")
    private UserProfileImageEntity profileImage;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    private UserActivity userActivity = UserActivity.NORMAL;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PostPersist
    private void setCreatedAt() {
        createdAt = LocalDateTime.now();
    }

    private UserEntity(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public static UserEntity of(String userName, String password, String nickName, String email) {
        return new UserEntity(userName, password, nickName, email);
    }

    public void uploadProfileImage(UserProfileImageEntity profileImage) {
        this.profileImage = profileImage;
    }

    public void removeProfileImage() {
        this.profileImage = null;
    }

    public void toAdmin() {
        this.role = UserRole.ADMIN;
    }

    public void changeActivity(UserActivity userActivity) {
        this.userActivity = userActivity;
    }
}
