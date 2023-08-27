package com.sns.yourconnection.service.thirdparty.telegram;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TelegramServiceTest {

    @Autowired
    private TelegramService telegramService;


    @Test
    void send_message() {
        telegramService.sendTelegram("message");
    }
}
