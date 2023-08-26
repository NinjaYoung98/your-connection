package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TranslateException extends RuntimeException {
    private ErrorCode errorCode;
}
