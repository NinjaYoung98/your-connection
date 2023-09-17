package com.sns.yourconnection.model.result.users.admin;

import com.sns.yourconnection.model.dto.Post;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostActivityResponse {

    private Long postId;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ContentActivity contentActivity;

    public static PostActivityResponse fromPost(Post post) {
        return new PostActivityResponse(
            post.getId(),
            post.getCreatedBy(),
            post.getUpdatedBy(),
            post.getCreatedAt(),
            post.getUpdatedAt(),
            post.getContentActivity()
        );
    }

}
