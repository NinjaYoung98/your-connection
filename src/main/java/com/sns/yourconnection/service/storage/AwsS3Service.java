package com.sns.yourconnection.service.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sns.yourconnection.common.properties.AwsProperties;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.utils.files.FileInfo;
import com.sns.yourconnection.service.storage.strategy.FileUploadStrategy;
import com.sns.yourconnection.utils.generator.FilenameGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service implements StorageService {

    private AmazonS3 amazonS3Client;
    private final AwsProperties awsProperties;
    private final List<FileUploadStrategy> strategies;

    @PostConstruct
    public void amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
            awsProperties.getAccessKey(),
            awsProperties.getSecretKey());

        amazonS3Client = AmazonS3ClientBuilder.standard()
            .withRegion(
                awsProperties.getRegionStatic())
            .withCredentials(
                new AWSStaticCredentialsProvider(awsCredentials))
            .build();
    }

    @Override
    public FileInfo upload(MultipartFile multipartFile) {
        return uploadFile(awsProperties.getImageBucket(), multipartFile);
    }

    @Override
    public List<FileInfo> uploadFiles(List<MultipartFile> multipartFile) {
        List<FileInfo> fileInfoList = new ArrayList<>();

        multipartFile.forEach(
            file -> {
                fileInfoList.add(uploadFile(awsProperties.getStorageBucket(), file));
            });

        log.info("ready set {} images to upload", fileInfoList.size());

        return fileInfoList;
    }

    private FileInfo uploadFile(String bucket, MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        String filename = FilenameGenerator.createFilename(originalFilename);

        FileUploadStrategy fileUploadStrategy = strategies.stream()
            .filter(
                strategy -> strategy.supports(file.getContentType()))
            .findFirst()
            .orElseThrow(
                () -> new AppException(ErrorCode.INVALID_FILE_TYPE));

        MultipartFile multiFile = fileUploadStrategy.uploadFile(file, filename);
        ObjectMetadata objectMetadata = putObjectMetadata(multiFile);

        try {
            amazonS3Client.putObject(
                new PutObjectRequest(bucket, filename, multiFile.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            String s3ClientUrl = amazonS3Client.getUrl(bucket, filename).toString();

            return FileInfo.of(
                originalFilename, filename, s3ClientUrl, fileUploadStrategy.getStorageType());

        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public void delete(String filename) {
        amazonS3Client.deleteObject(awsProperties.getImageBucket(), filename);
    }

    @Override
    public void deleteFiles(String filename) {
        amazonS3Client.deleteObject(awsProperties.getStorageBucket(), filename);
    }

    private ObjectMetadata putObjectMetadata(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());
        return objectMetadata;
    }
}
