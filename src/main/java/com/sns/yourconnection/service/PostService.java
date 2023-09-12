package com.sns.yourconnection.service;


import com.sns.yourconnection.model.dto.Post;
import com.sns.yourconnection.model.dto.PostStorage;
import com.sns.yourconnection.model.entity.post.PostCountEntity;
import com.sns.yourconnection.model.entity.post.PostLogEntity;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.entity.post.PostStorageEntity;
import com.sns.yourconnection.model.param.storage.FileInfo;
import com.sns.yourconnection.repository.*;
import com.sns.yourconnection.model.param.post.PostRequest;

import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.user.UserEntity;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.service.storage.StorageService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCountRepository postCountRepository;
    private final PostStorageRepository postStorageRepository;
    private final StorageService storageService;

    @Transactional
    public Post createPost(PostRequest postCreateRequest, User user,
        List<MultipartFile> multipartFiles) {

        UserEntity userEntity = getUserEntity(user);

        PostEntity postEntity = PostEntity.of(postCreateRequest.getTitle(),
            postCreateRequest.getContent(), userEntity);

        if (multipartFiles != null && !CollectionUtils.isEmpty(multipartFiles)) {
            saveFilesToS3(multipartFiles, postEntity);
        }
        postRepository.save(postEntity);
        return Post.fromEntity(postEntity);
    }

    @Transactional(readOnly = true)
    public Page<Post> getPostPage(Pageable pageable) {
        //  post 조회 기능
        return postRepository.findAll(pageable).map(Post::fromEntity);
    }


    @Transactional
    public Post getPost(Long postId, User user) {
         /*
        특정 post 조회 기능
            -  post 존재하는지 확인
            -  post 에 대한 user 조회 기록 체크
            -  조회 기록이 있다면 조회수 오르지 x
            -  조회 기록이 없다면 조회수 1 증가
         */

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
        UserEntity userEntity = getUserEntity(user);
        setPostCount(postEntity, userEntity);
        return Post.fromEntity(postEntity);
    }

    @Transactional(readOnly = true)
    public Page<Post> searchPostPage(String keyword, Pageable pageable) {
        // title에 키워드를 포함한 postEntity를 반환한다
        return postRepository.searchByKeyword(keyword, pageable).map(Post::fromEntity);
    }

    @Transactional
    public Post updatePost(Long postId, PostRequest postUpdateRequest, User user,
        List<MultipartFile> multipartFiles) {
        /*
        post 를 수정한다
            - post 존재하는지 확인한다.
            - post 작성자만 해당 post 를 수정할 수 있다.
            - post 수정한다.
            - post에 저장된 정적 storage 파일들이 존재 할 경우 update 합니다.
                - 새로운 파일들이 update 되었다면 기존 파일들을 delete 합니다.
                - AWS S3 버킷의 비용적인 문제가 야기된다면, 변경 점을 체크하는 로직 ,
                  또는 글라시어 클래스를 고려해 볼 수 있습니다.
         */

        PostEntity postEntity = getPostEntity(postId);
        validateMatches(user, postEntity);

        postEntity.update(postUpdateRequest.getTitle(), postUpdateRequest.getContent());

        if (multipartFiles != null && !CollectionUtils.isEmpty(multipartFiles)) {
            updateFilesToS3(multipartFiles, postEntity);
        }
        return Post.fromEntity(postEntity);
    }

    @Transactional
    public void saveFilesToS3(List<MultipartFile> multipartFiles,
        PostEntity postEntity) {

        List<FileInfo> fileInfoList = storageService.uploadFiles(multipartFiles);

        List<PostStorageEntity> postStorageEntities = fileInfoList.stream()
            .map(fileInfo -> createPostStorageEntity(postEntity, fileInfo))
            .collect(Collectors.toList());

        List<PostStorageEntity> newPostStorageEntities = postStorageRepository.saveAll(
            postStorageEntities);
        postEntity.updateStorage(newPostStorageEntities);
    }

    @Transactional
    public void updateFilesToS3(List<MultipartFile> multipartFiles, PostEntity postEntity) {
        List<PostStorageEntity> oldPostStorageEntities = postStorageRepository.findByPost(
            postEntity);

        saveFilesToS3(multipartFiles, postEntity);

        oldPostStorageEntities.stream().forEach(
            oldPostStorage -> {
                storageService.deleteFiles(oldPostStorage.getStoreFilename());
            });
    }


    @Transactional
    public void deletePost(Long postId, User user) {
        /*
        post 를 삭제한다.
            - post 존재하는지 확인한다.
            - post 작성자만 해당 post 를 삭제할 수 있다.
            - post 삭제한다.
         */
        PostEntity postEntity = getPostEntity(postId);
        validateMatches(user, postEntity);
        postStorageRepository.findByPost(postEntity).stream().forEach(postStorageEntity -> {
            storageService.deleteFiles(postStorageEntity.getStoreFilename());
        });

        postRepository.delete(postEntity);
    }

    @Transactional
    public void setPostCount(PostEntity postEntity, UserEntity userEntity) {
        if (postCountRepository.findByPostAndUser(postEntity, userEntity).isEmpty()) {
            postCountRepository.save(PostCountEntity.of(userEntity, postEntity));
            log.info("Increase post count for post: {} by user: {}", postEntity.getId(),
                userEntity.getId());
        }
    }

    @Transactional(readOnly = true)
    public Integer getPostCount(Long postId) {
        return postRepository.fetchJoinIdWithPostCount(postId)
            .map(PostEntity::getPostCounts)
            .map(postCount -> postCount.size())
            .orElseThrow(() ->
                new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    public PostEntity getPostEntity(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
            new AppException(ErrorCode.POST_DOES_NOT_EXIST));
    }

    private PostStorageEntity createPostStorageEntity(PostEntity postEntity, FileInfo fileInfo) {
        return PostStorageEntity.of(
            fileInfo.getOriginalFilename(),
            fileInfo.getStoreFilename(),
            fileInfo.getPathUrl(),
            fileInfo.getStorageType(),
            postEntity);
    }

    public UserEntity getUserEntity(User user) {
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public void validateMatches(User user, PostEntity postEntity) {
        if (postEntity.getUser().getId() != user.getId()) {
            throw new AppException(ErrorCode.HAS_NOT_PERMISSION_TO_ACCESS);
        }
    }
}
