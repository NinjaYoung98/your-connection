package com.sns.yourconnection.model.post.result;

import com.sns.yourconnection.model.post.dto.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateResponse {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public static PostUpdateResponse fromPost(Post post) {
        return new PostUpdateResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getUpdatedAt(),
            post.getUpdatedBy()
        );
    }
}
