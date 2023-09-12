package com.sns.yourconnection.service.thirdparty.notifitation;

import com.sns.yourconnection.service.notifitation.TelegramService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TelegramServiceTest {

    @Autowired
    private TelegramService telegramService;


    @Test
    void send_message() {
        telegramService.sendMessage("message");
    }
}
