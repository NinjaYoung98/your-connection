package com.sns.yourconnection.service.storage;

import com.sns.yourconnection.utils.files.FileInfo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    FileInfo upload(MultipartFile multipartFile);

    List<FileInfo> uploadFiles(List<MultipartFile> multipartFile);

    void delete(String key);

    void deleteFiles(String key);

}
