package com.sns.yourconnection.controller;

import static com.sns.yourconnection.controller.response.ResponseSuccess.response;

import com.sns.yourconnection.common.annotation.AuthUser;
import com.sns.yourconnection.controller.response.ResponseSuccess;
import com.sns.yourconnection.model.dto.User;
import com.sns.yourconnection.model.param.translate.TranslateRequest;
import com.sns.yourconnection.model.result.translate.TranslateResponse;
import com.sns.yourconnection.service.translator.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
@Slf4j
public class TranslateApiController {

    private final TranslateService translateService;

    @PostMapping("")
    public ResponseSuccess<TranslateResponse> supportTranslate(@AuthUser User user,
        @RequestBody TranslateRequest translateRequest, @RequestParam String source,
        @RequestParam String target) {

        log.info("[Translate] translate text with User: "
            + "{}, source {}, target: {} ", user.getId(), source, target);

        TranslateResponse translateResponse = TranslateResponse.of(
            translateService.supportTranslate(
                translateRequest.getText(), source, target));

        log.info("[Translate] translated text safety [detail : before: {} after: {}  ",
            translateRequest.getText(), translateResponse.getText());

        return response(translateResponse);
    }
}
