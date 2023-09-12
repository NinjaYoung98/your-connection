package com.sns.yourconnection.service.notifitation;

import com.sns.yourconnection.common.properties.TelegramProperties;
import com.sns.yourconnection.exception.ErrorCode;
import com.sns.yourconnection.exception.TelegramException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService implements NotificationService{

    private final TelegramProperties properties;
    private final Environment environment;
    private final RestTemplate restTemplate;
    @Override
    public void sendMessage(String message) {
        message = environment.getProperty("spring.config.activate.on-profile") + message;
        try {
            sendTelegram(properties, message);
            log.info(message);
        } catch (Exception e) {
            throw new TelegramException(ErrorCode.TELEGRAM_SEND_ERROR);
        }
    }

    private void sendTelegram(TelegramProperties properties, String message) {
        try {
            final String url = properties.getUrl();
            final String chatId = properties.getChatId();
            final HttpHeaders headers = new HttpHeaders();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

            CloseableHttpClient httpClient = getHttpClient();
            requestFactory.setHttpClient(httpClient);

            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("chat_id", chatId)
                .queryParam("parse_mode", "HTML")
                .queryParam("disable_web_page_preview", "true")
                .queryParam("text", message);
            final HttpEntity<?> entity = new HttpEntity<>(headers);

            restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity,
                String.class);
        } catch (Exception e) {
            throw new TelegramException(ErrorCode.TELEGRAM_SEND_ERROR);
        }
    }

    private CloseableHttpClient getHttpClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLHostnameVerifier(new NoopHostnameVerifier())
            .build();
        return httpClient;
    }

}