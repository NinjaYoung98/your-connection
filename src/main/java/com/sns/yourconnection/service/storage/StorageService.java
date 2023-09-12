package com.sns.yourconnection.service.storage;

import com.sns.yourconnection.model.param.storage.FileInfo;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    FileInfo upload(MultipartFile multipartFile);

    List<FileInfo> uploadFiles(List<MultipartFile> multipartFile);

    void delete(String fileName);

    void deleteFiles(String fileName);

}
