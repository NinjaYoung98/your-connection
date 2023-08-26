package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.comment.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Comment {

    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private User user;
    private Post post;

    public static Comment fromEntity(CommentEntity comment) {
        return new Comment(
            comment.getId(),
            comment.getComment(),
            comment.getCreatedAt(),
            comment.getCreatedAt(),
            comment.getCreatedBy(),
            comment.getUpdatedBy(),
            User.fromEntity(comment.getUser()),
            Post.fromEntity(comment.getPost())
        );
    }
}
