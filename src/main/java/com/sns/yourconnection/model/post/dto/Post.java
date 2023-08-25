package com.sns.yourconnection.model.post.dto;

import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String content;
    private User user;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContent(),
            User.fromEntity(postEntity.getUser())
        );
    }

    public Post translateContent(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }
}
