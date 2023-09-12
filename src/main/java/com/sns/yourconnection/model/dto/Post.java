package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.post.PostEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<PostStorage> postStorage;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getContent(),
            postEntity.getCreatedAt(),
            postEntity.getUpdatedAt(),
            postEntity.getCreatedBy(),
            postEntity.getUpdatedBy(),
            User.fromEntity(postEntity.getUser()),
            PostStorageEntityToDTO(postEntity)
        );
    }

    private static List<PostStorage> PostStorageEntityToDTO(PostEntity postEntity) {

        return postEntity.getPostStorage().stream()
            .map(PostStorage::fromEntity)
            .collect(Collectors.toList());
    }
}