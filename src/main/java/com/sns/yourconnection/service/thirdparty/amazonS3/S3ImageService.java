package com.sns.yourconnection.service.thirdparty.amazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ImageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3Client;
    private static final String[] SUPPORT_IMAGE_EXTENSION = {".jpg", ".jpeg", ".png"};
    private static final int RESIZE_TARGET_WIDTH = 768;

    /**
     * 파일들을 Amazon S3에 업로드하고 업로드된 파일들의 이름 목록을 반환합니다.
     *
     * @param multipartFile 업로드할 MultipartFile 객체의 리스트입니다. 각 파일은 리사이징되고 확장자 이름을 기반한 파일명이 생성됩니다.
     * @return 성공적으로 업로드된 파일들의 이름 목록을 return 합니다.
     * @throws AppException 이미지 형식이 지원되지 않거나 이미지 업로드가 실패했을 경우 예외를 발생시킵니다.
     */
    public List<String> upload(List<MultipartFile> multipartFile) {
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            log.info(" original file name : {}", file.getOriginalFilename());
            String fileName = createFileName(file.getOriginalFilename());
            log.info(" create file name from origin : {}", fileName);

            String fileFormatName = file.getContentType()
                .substring(file.getContentType().lastIndexOf("/") + 1);
            log.info("file format name from contentType : {}", fileFormatName);

            MultipartFile resizedFile = resizeImage(fileName, fileFormatName, file);
            ObjectMetadata objectMetadata = generateObjectMetadata(resizedFile);

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new AppException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
            fileNameList.add(fileName);
        });
        log.info("ready set {} images to upload", fileNameList.size());
        return fileNameList;
    }

    /**
     * Amazon S3 버킷에서 주어진 이름의 파일을 삭제합니다.
     *
     * @param fileName 삭제하려는 파일의 이름입니다.
     */
    public void deleteImage(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
    }

    private String createFileName(String fileName) {
        return UUID.randomUUID() + "-" + getFileExtension(fileName);
    }

    private String getFileExtension(String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            log.info("extension for upload image : {}", extension);
            validateExtension(extension);
            return extension;
        } catch (StringIndexOutOfBoundsException e) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT,
                "not support this fileName: " + fileName);
        }
    }

    private void validateExtension(String extension) {
        if (!isSupportExtension(extension)) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT,
                "not support this file extension: " + extension);
        }
    }

    private boolean isSupportExtension(String extension) {
        return Arrays.stream(SUPPORT_IMAGE_EXTENSION)
            .anyMatch(supportExtension -> supportExtension.equals(extension));
    }

    private ObjectMetadata generateObjectMetadata(MultipartFile resizedFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(resizedFile.getSize());
        objectMetadata.setContentType(resizedFile.getContentType());
        return objectMetadata;
    }

    private MultipartFile resizeImage(String fileName, String fileFormatName,
        MultipartFile originalImage) {
        try {
            // MultipartFile -> BufferedImage Convert
            BufferedImage bufferedOriginImage = convertToBufferImage(originalImage);

            if (!isResizeTargetWidth(bufferedOriginImage)) {
                return originalImage;
            }
            MarvinImage imageMarvin = resizeMarvinImage(bufferedOriginImage);
            ByteArrayOutputStream byteArrayOutputStream = convertToByteArray(fileFormatName,
                imageMarvin);
            return new MockMultipartFile(fileName, byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_RESIZE_IMAGE);
        }
    }

    private BufferedImage convertToBufferImage(MultipartFile originalImage)
        throws IOException {
        BufferedImage bufferedOriginImage = ImageIO.read(originalImage.getInputStream());
        return bufferedOriginImage;
    }

    private boolean isResizeTargetWidth(BufferedImage bufferedOriginImage) {
        if (bufferedOriginImage.getWidth() < RESIZE_TARGET_WIDTH) {
            return false;
        }
        return true;
    }

    private MarvinImage resizeMarvinImage(BufferedImage bufferedOriginImage) {
        MarvinImage imageMarvin = new MarvinImage(bufferedOriginImage);
        Scale scale = new Scale();
        scale.load();
        // newWidth : newHeight = originWidth : originHeight
        int targetHeight =
            RESIZE_TARGET_WIDTH * bufferedOriginImage.getHeight() / bufferedOriginImage.getWidth();
        scale.setAttribute("newWidth", RESIZE_TARGET_WIDTH);
        scale.setAttribute("newHeight", targetHeight);

        scale.process(imageMarvin.clone(), imageMarvin, null, null, false);
        return imageMarvin;
    }

    private ByteArrayOutputStream convertToByteArray(String fileFormatName,
        MarvinImage imageMarvin) throws IOException {
        BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageNoAlpha, fileFormatName, byteArrayOutputStream);

        byteArrayOutputStream.flush();

        return byteArrayOutputStream;
    }
}
