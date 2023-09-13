package com.sns.yourconnection.model.entity.users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user_profile_image\"")
public class UserProfileImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;

    private String storeFilename;

    private String urlPath;

    @OneToOne(mappedBy = "profileImage") //호출될 시 프록시 객체 만들 수 없음, 지연로딩 불가
    private UserEntity user;

    private UserProfileImageEntity(String originalFilename, String storeFilename, String urlPath,
        UserEntity user) {
        this.originalFilename = originalFilename;
        this.storeFilename = storeFilename;
        this.urlPath = urlPath;
        this.user = user;
    }

    public static UserProfileImageEntity of(String originalFilename, String storeFilename,
        String urlPath, UserEntity user) {
        return new UserProfileImageEntity(originalFilename, storeFilename, urlPath, user);
    }
}
