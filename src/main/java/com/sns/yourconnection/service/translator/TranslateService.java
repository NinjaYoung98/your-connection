package com.sns.yourconnection.service.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.yourconnection.common.properties.TranslateProperties;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.exception.TranslateException;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranslateService {

    private final ObjectMapper objectMapper;
    private final TranslateProperties properties;

    public String supportTranslate(String text, String source, String target) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType,
            "{\r\n    \"text\": \"" + text + "\",\r\n    \"source\": \"" + source
                + "\",\r\n    \"target\": \"" + target + "\"\r\n}");
        Response response = requestBuild(client, body);
        return extractText(response);
    }

    private Response requestBuild(OkHttpClient client, RequestBody body) {
        try {
            Request request = new Request.Builder()
                .url("https://deepl-translator1.p.rapidapi.com/translate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("X-RapidAPI-Key", properties.getKey())
                .addHeader("X-RapidAPI-Host", properties.getHost())
                .build();
            Response response = client.newCall(request).execute();

            log.info("deepl api response has been successfully build");
            return response;
        } catch (Exception e) {
            throw new TranslateException(ErrorCode.TRANSLATION_BUILD_PROCESSING_ERROR);
        }
    }

    private String extractText(Response response) {
        try {
            Map<String, Object> result = objectMapper.readValue(response.body().string(),
                Map.class);
            validateResultValue(result);
            return result.get("text").toString();
        } catch (IOException e) {
            throw new TranslateException(ErrorCode.TRANSLATION_PARSE_PROCESSING_ERROR);
        }
    }

    private static void validateResultValue(Map<String, Object> result) {
        if (!result.containsKey("text") || result.get("text") == null) {
            throw new TranslateException(ErrorCode.TRANSLATION_PARSE_NO_TEXT_FOUND);
        }
    }
}