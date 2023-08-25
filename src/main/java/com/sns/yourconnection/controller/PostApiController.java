package com.sns.yourconnection.controller;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.model.post.param.PostRequest;
import com.sns.yourconnection.model.user.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostApiController {

    @PostMapping("")
    public void createPost(@RequestBody PostRequest postCreateRequest,
        @AuthUser User user) {
        log.info("Create a new post for user: {}. Request details: postCreateRequest: {}",
            user.getId(), postCreateRequest);
    }
}
