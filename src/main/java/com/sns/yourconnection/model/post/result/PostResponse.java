package com.sns.yourconnection.model.post.result;

import com.sns.yourconnection.model.post.dto.Post;
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

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent()
        );
    }
}