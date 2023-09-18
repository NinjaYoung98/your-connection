package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.common.storage.StorageType;
import com.sns.yourconnection.model.entity.post.PostStorageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostStorage {

    private Long id;

    private String originFilename;

    private String pathUrl;

    private StorageType storageType;

    public static PostStorage fromEntity(PostStorageEntity postStorageEntity) {
        return new PostStorage(
            postStorageEntity.getId(),
            postStorageEntity.getOriginFilename(),
            postStorageEntity.getStoreFilename(),
            postStorageEntity.getStorageType());
    }
}
