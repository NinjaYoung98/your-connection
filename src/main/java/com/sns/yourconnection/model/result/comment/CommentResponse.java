package com.sns.yourconnection.model.result.comment;

import com.sns.yourconnection.model.dto.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Long postId;
    private Long commentId;
    private String comment;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
            comment.getPost().getId(),
            comment.getId(),
            comment.getComment(),
            comment.getCreatedBy(),
            comment.getUpdatedBy(),
            comment.getCreatedAt(),
            comment.getUpdatedAt()
        );
    }
}
