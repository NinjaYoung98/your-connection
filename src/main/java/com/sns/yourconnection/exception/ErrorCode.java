package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "status code 500 is occurred"),

    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "username is duplicated"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "nickname is duplicated"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "invalid password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found");

    private final HttpStatus httpStatus;
    private final String message;
}
