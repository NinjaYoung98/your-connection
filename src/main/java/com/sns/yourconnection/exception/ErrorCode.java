package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "status code 500 is occurred"),

    INVALID_TOKEN_FORMED(HttpStatus.UNAUTHORIZED, "not support to this token`s formed"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "token is expired"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "token signature is invalid"),
    NOT_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "token is not start with BEARER or null"),

    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "username is duplicated"),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "nickname is duplicated"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "invalid password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    POST_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "post does not exist"),
    COMMENT_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "comment does not exist"),
    PAGE_SIZE_NOT_APPLICABLE(HttpStatus.BAD_REQUEST, "page size is too large"),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.CONFLICT, "cannot follow yourself"),
    HAS_NOT_PERMISSION_TO_ACCESS(HttpStatus.UNAUTHORIZED, "has not permission to access"),
    NONE_MATCH(HttpStatus.NOT_FOUND, "can not find any match");


    private final HttpStatus httpStatus;
    private final String message;
}
