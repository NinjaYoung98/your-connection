package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.common.annotation.ValidatedPageRequest;
import com.sns.yourconnection.controller.response.PageResponseWrapper;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.dto.Comment;
import com.sns.yourconnection.model.param.comment.CommentRequest;
import com.sns.yourconnection.model.result.comment.CommentResponse;
import com.sns.yourconnection.model.dto.Post;
import com.sns.yourconnection.model.param.post.PostRequest;
import com.sns.yourconnection.model.result.post.PostResponse;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.service.post.CommentService;
import com.sns.yourconnection.service.post.PostLikeService;
import com.sns.yourconnection.service.post.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostApiController {

    private final PostService postService;
    private final CommentService commentService;
    private final PostLikeService postLikeService;

    @PostMapping("")
    public ResponseSuccess<PostResponse> createPost(
        @RequestPart(value = "request") PostRequest postCreateRequest,
        @RequestPart(value = "storage", required = false) List<MultipartFile> multipartFiles,
        @AuthUser User user) {

        log.info("Create a new post for user: {}. Request details: postCreateRequest: {}",
            user.getId(), postCreateRequest);

        Post post = postService.createPost(postCreateRequest, user, multipartFiles);
        PostResponse postResponse = PostResponse.fromPost(post);

        return response(postResponse);
    }

    @GetMapping("{postId}")
    public ResponseSuccess<PostResponse> getPost(@PathVariable Long postId, @AuthUser User user) {

        log.info("getting post with ID: {} for user: {}", postId, user.getId());

        Post post = postService.getPost(postId, user);
        return response(PostResponse.fromPost(post));
    }

    @PutMapping("{postId}")
    public ResponseSuccess<PostResponse> updatePost(@PathVariable Long postId,
        @RequestPart(value = "request") PostRequest postUpdateRequest,
        @RequestPart(value = "storage", required = false) List<MultipartFile> multipartFiles,
        @AuthUser User user) {

        log.info("Updating post with ID: {} for user: {} Request details: postUpdateRequest: {}",
            postId, user.getId(), postUpdateRequest);

        Post post = postService.updatePost(postId, postUpdateRequest, user, multipartFiles);
        return response(PostResponse.fromPost(post));
    }

    @DeleteMapping("{postId}")
    public ResponseSuccess<Void> deletePost(@PathVariable Long postId, @AuthUser User user) {

        log.info("Deleting post with ID: {} for user: {}", postId, user.getId());

        postService.deletePost(postId, user);
        return response();
    }

    @PostMapping("{postId}/comment")
    public ResponseSuccess<CommentResponse> createComment(@PathVariable Long postId,
        @RequestBody CommentRequest commentRequest, @AuthUser User user) {

        log.info("Creat a new comment for post: {} by user: {} Request detail: {}", postId,
            user.getId(), commentRequest);

        Comment comment = commentService.createComment(postId, commentRequest, user);
        return response(CommentResponse.fromComment(comment));
    }

    @GetMapping("{postId}/comment")
    public ResponseSuccess<List<CommentResponse>> getCommentPage(
        @PathVariable Long postId, @ValidatedPageRequest Pageable pageable) {

        log.info("Retrieving comments for post with ID: {}. Pageable details: {}", postId,
            pageable);

        Page<CommentResponse> commentPage = commentService.getCommentPage(postId, pageable)
            .map(CommentResponse::fromComment);

        return response(commentPage.getContent(), PageResponseWrapper.fromPage(commentPage));
    }

    @GetMapping("/search/{keyword}")
    public ResponseSuccess<List<PostResponse>> searchPostPage(
        @PathVariable String keyword, @ValidatedPageRequest Pageable pageable) {

        log.info("Searching post with keyword: '{}' and pageable: {}", keyword, pageable);

        Page<PostResponse> postPage = postService.searchPostPage(keyword, pageable)
            .map(PostResponse::fromPost);
        return response(postPage.getContent(), PageResponseWrapper.fromPage(postPage));
    }

    @PutMapping("/{postId}/comment/{commentId}")
    public ResponseSuccess<CommentResponse> updateComment(
        @PathVariable Long postId, @PathVariable Long commentId,
        @RequestBody CommentRequest commentRequest, @AuthUser User user) {

        log.info("Updating comment with ID: {} for post:{} by user: {} Request detail: {}",
            commentId, postId, user.getId(), commentRequest);

        Comment comment = commentService.updateComment(postId, commentId, commentRequest, user);
        return response(CommentResponse.fromComment(comment));
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseSuccess<Void> deleteComment(@PathVariable Long postId,
        @PathVariable Long commentId, @AuthUser User user) {

        log.info("Deleting comment with ID: {} for post: {} by user: {}",
            commentId, postId, user.getId());

        commentService.deleteComment(postId, commentId, user);
        return response();
    }

    @PostMapping("{postId}/like")
    public ResponseSuccess<Void> setLikeCount(@PathVariable Long postId, @AuthUser User user) {

        log.info("Setting like count for post with ID: {} and user: {}", postId, user.getId());

        postLikeService.setLikeCount(postId, user);
        return response();
    }
}
