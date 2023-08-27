package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TelegramException extends RuntimeException {
    private ErrorCode errorCode;
}
