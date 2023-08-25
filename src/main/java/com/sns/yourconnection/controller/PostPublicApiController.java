package com.sns.yourconnection.controller;

import com.sns.yourconnection.common.annotation.ValidatedPageRequest;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.controller.response.PageResponseWrapper;
import com.sns.yourconnection.model.post.result.PostResponse;
import com.sns.yourconnection.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;

@RestController
@RequestMapping("/public-api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostPublicApiController {

    private final PostService postService;

    @GetMapping("")
    public ResponseSuccess<List<PostResponse>> getPostPage(
        @ValidatedPageRequest Pageable pageable) {
        log.info("Getting post page. Pageable details: {}", pageable);

        Page<PostResponse> postPage = postService.getPostPage(pageable).map(PostResponse::fromPost);
        log.info("Retrieved post page successfully. page size: {}", postPage.getSize());

        return response(postPage.getContent(), PageResponseWrapper.fromPage(postPage));
    }
}
