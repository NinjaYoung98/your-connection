package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.post.dto.Post;
import com.sns.yourconnection.model.post.param.PostRequest;
import com.sns.yourconnection.model.post.result.PostResponse;
import com.sns.yourconnection.model.user.dto.User;
import com.sns.yourconnection.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostApiController {

    private final PostService postService;

    @PostMapping("")
    public ResponseSuccess<PostResponse> createPost(@RequestBody PostRequest postCreateRequest,
        @AuthUser User user) {
        log.info("Create a new post for user: {}. Request details: postCreateRequest: {}",
            user.getId(), postCreateRequest);

        Post post = postService.createPost(postCreateRequest, user);
        PostResponse postResponse = PostResponse.fromPost(post);
        log.info("Post created successfully. Post details:{}", postResponse);

        return response(postResponse);
    }
}
