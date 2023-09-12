package com.sns.yourconnection.service.storage.strategy;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.model.entity.common.StorageType;
import com.sns.yourconnection.utils.FilenameGenerator;
import com.sns.yourconnection.utils.ImageResizer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ImageUploadStrategy implements FileUploadStrategy {

    @Override
    public boolean supports(String contentType) {

        return contentType != null && contentType.startsWith("image/");
    }

    @Override
    public MultipartFile uploadFile(MultipartFile file, String filename) {

        log.info(" create file name from origin : {}", filename);
        String fileFormatName = FilenameGenerator.extractFormat(file);
        try (InputStream inputStream = file.getInputStream()) {

            return ImageResizer.resizeImage(filename, fileFormatName, file,
                convertToBufferImage(inputStream));

        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.IMAGE;
    }

    private BufferedImage convertToBufferImage(InputStream inputStream)
        throws IOException {
        return ImageIO.read(inputStream);
    }
}
