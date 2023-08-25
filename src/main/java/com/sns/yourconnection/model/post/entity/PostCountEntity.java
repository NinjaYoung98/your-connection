package com.sns.yourconnection.model.post.entity;

import com.sns.yourconnection.model.user.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"post_count\"")
public class PostCountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private PostCountEntity(UserEntity user, PostEntity post) {
        this.user = user;
        this.post = post;
        this.post.getPostCounts().add(this);
    }

    public static PostCountEntity of(UserEntity user, PostEntity post) {
        return new PostCountEntity(user, post);
    }
}
