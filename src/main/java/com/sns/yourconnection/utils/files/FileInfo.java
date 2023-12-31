package com.sns.yourconnection.utils.files;

import com.sns.yourconnection.model.entity.common.storage.StorageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {

    public static final FileInfo EMPTY = FileInfo.of("", "", "", StorageType.EMPTY);

    private String originalFilename;
    private String storeFilename;
    private String pathUrl;
    private StorageType storageType;

    public static FileInfo of(String originalFilename, String storeFilename, String pathUrl,
        StorageType storageType) {
        return new FileInfo(originalFilename, storeFilename, pathUrl, storageType);
    }
}
