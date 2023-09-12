package com.sns.yourconnection.model.result.storage;

import com.sns.yourconnection.model.param.storage.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoResponse {

    private String originalFilename;
    private String storeFilename;

    public static FileInfoResponse fromFileInfo(FileInfo fileInfo) {
        return new FileInfoResponse(fileInfo.getOriginalFilename(), fileInfo.getStoreFilename());
    }
}
