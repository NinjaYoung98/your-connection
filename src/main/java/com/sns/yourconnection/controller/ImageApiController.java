package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;

import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.service.thirdparty.amazonS3.S3ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Slf4j
public class ImageApiController {

    private final S3ImageService s3ImageService;

    @PostMapping("")
    public ResponseSuccess<List<String>> uploadImage(
        @RequestParam("images") List<MultipartFile> multipartFile) {
        log.info("[ImageApiController] upload image ");
        return response(s3ImageService.upload(multipartFile));
    }

    @DeleteMapping("")
    public ResponseSuccess<Void> deleteImage(@RequestParam String fileName) {
        log.info("[ImageApiController] delete image");
        s3ImageService.deleteImage(fileName);
        return response();
    }
}
