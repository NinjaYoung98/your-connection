package com.sns.yourconnection.utils;

import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class ImageResizer {

    private static final int RESIZE_TARGET_WIDTH = 768;

    public static MultipartFile resizeImage(String filename, String fileFormatName,
        MultipartFile originalImage, BufferedImage bufferedImage) {
        try {

            if (!isResizeTargetWidth(bufferedImage)) {
                return originalImage;
            }
            MarvinImage imageMarvin = resizeMarvinImage(bufferedImage);
            ByteArrayOutputStream byteArrayOutputStream = convertToByteArray(fileFormatName,
                imageMarvin);
            return new MockMultipartFile(filename, byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new AppException(ErrorCode.FAILED_RESIZE_IMAGE);
        }
    }

    private static MarvinImage resizeMarvinImage(BufferedImage bufferedOriginImage) {
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


    private static boolean isResizeTargetWidth(BufferedImage bufferedOriginImage) {
        if (bufferedOriginImage.getWidth() < RESIZE_TARGET_WIDTH) {
            return false;
        }
        return true;
    }

    private static ByteArrayOutputStream convertToByteArray(String fileFormatName,
        MarvinImage imageMarvin) throws IOException {
        BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(imageNoAlpha, fileFormatName, byteArrayOutputStream);

        byteArrayOutputStream.flush();

        return byteArrayOutputStream;
    }
}
