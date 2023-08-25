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

    @GetMapping("{postId}")
    public ResponseSuccess<PostResponse> getPost(@PathVariable Long postId, @AuthUser User user) {
        log.info("getting post with ID: {} for user: {}", postId, user.getId());

        Post post = postService.getPost(postId, user);
        log.info("Retrieved post ID: {}", post.getId());

        return response(PostResponse.fromPost(post));
    }

    @PutMapping("{postId}")
    public ResponseSuccess<PostResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequest postUpdateRequest, @AuthUser User user) {
        log.info("Updating post with ID: {} for user: {} Request details: postUpdateRequest: {}", postId, user.getId(), postUpdateRequest);

        Post post = postService.updatePost(postId, postUpdateRequest, user);
        PostResponse postResponse = PostResponse.fromPost(post);
        log.info("Post with ID: {} updated successfully. Updated post details: {}", post.getId(), postResponse);

        return response(PostResponse.fromPost(post));
    }
}
