package com.sns.yourconnection.model.result.post;

import com.sns.yourconnection.model.dto.Post;
import com.sns.yourconnection.model.dto.PostStorage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private List<PostStorage> postStorageList;


    public static PostResponse fromPost(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            post.getCreatedAt(),
            post.getCreatedBy(),
            post.getUpdatedAt(),
            post.getUpdatedBy(),
            post.getPostStorage()
        );
    }
}
