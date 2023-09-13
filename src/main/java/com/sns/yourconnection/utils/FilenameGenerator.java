package com.sns.yourconnection.utils;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FilenameGenerator {

    private static final String[] SUPPORT_EXTENSION = {".jpg", ".jpeg", ".png"};

    public static String createFilename(String originFilename) {
        return UUID.randomUUID() + "-" + getFileExtension(originFilename);
    }

    private static String getFileExtension(String filename) {
        try {
            String extension = filename.substring(filename.lastIndexOf("."));
            log.info("extension for upload image : {}", extension);
            validateExtension(extension);
            return extension;
        } catch (StringIndexOutOfBoundsException e) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT,
                "not support this filename: " + filename);
        }
    }

    private static void validateExtension(String extension) {
        if (!isSupportExtension(extension)) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT,
                "not support this file extension: " + extension);
        }
    }

    private static boolean isSupportExtension(String extension) {
        return Arrays.stream(SUPPORT_EXTENSION)
            .anyMatch(supportExtension -> supportExtension.equals(extension));
    }

    public static String extractFormat(MultipartFile file) {
        String fileFormatName = file.getContentType()
            .substring(file.getContentType().lastIndexOf("/") + 1);
        return fileFormatName;
    }
}
