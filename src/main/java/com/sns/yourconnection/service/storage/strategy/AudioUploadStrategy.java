package com.sns.yourconnection.service.storage.strategy;

import com.sns.yourconnection.model.entity.common.storage.StorageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AudioUploadStrategy implements FileUploadStrategy {

    @Override
    public boolean supports(String contentType) {
        return "audio/mpeg".equals(contentType);
    }

    @Override
    public MultipartFile uploadFile(MultipartFile file,String filename) {
        // Implement the logic to process and upload audio files...
        return file;
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.AUDIO;
    }
}
