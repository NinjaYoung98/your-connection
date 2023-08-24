package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "status code 500 is occurred"),

    TELEGRAM_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred while sending a message on telegram"),
    TRANSLATION_TEXT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred while translating the text."),
    TRANSLATION_RESPONSE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "translate response processing failed"),

    INVALID_TOKEN_FORMED(HttpStatus.UNAUTHORIZED, "not support to this token`s formed"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "token is expired"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "token signature is invalid"),
    NOT_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "token is not start with BEARER or null"),

    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "username is duplicated"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "nickname is duplicated"),
    OVER_LENGTH(HttpStatus.BAD_REQUEST, "length exceeded"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    POST_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "post does not exist"),
    COMMENT_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "comment does not exist"),
    HAS_NOT_PERMISSION_TO_ACCESS(HttpStatus.UNAUTHORIZED, "has not permission to access"),
    PAGE_SIZE_NOT_APPLICABLE(HttpStatus.BAD_REQUEST, "page size is too large"),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.CONFLICT, "cannot follow yourself"),
    NONE_MATCH(HttpStatus.NOT_FOUND, "can not find any match"),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "too many request"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "invalid password"),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "no such algorithm"),
    NOT_SUPPORT_FORMAT(HttpStatus.BAD_REQUEST, "not support to this format");


    private final HttpStatus httpStatus;
    private final String message;
}
