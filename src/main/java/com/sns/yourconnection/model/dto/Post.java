package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.post.PostEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private User user;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContent(),
            postEntity.getCreatedAt(),
            postEntity.getUpdatedAt(),
            postEntity.getCreatedBy(),
            postEntity.getUpdatedBy(),
            User.fromEntity(postEntity.getUser())
        );
    }
}
