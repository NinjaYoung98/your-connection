package com.sns.yourconnection.service.storage.strategy;

import com.sns.yourconnection.model.entity.common.StorageType;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadStrategy {

    boolean supports(String contentType);

    MultipartFile uploadFile(MultipartFile file, String filename);

    StorageType getStorageType();
}
