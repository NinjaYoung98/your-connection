package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //internal server error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "status code 500 is occurred"),

    DATABASE_ACCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "database error"),

    TELEGRAM_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
        "error occurred while sending a message on telegram"),
    //translate
    TRANSLATION_PARSE_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
        "error occurred while translating the text."),
    TRANSLATION_BUILD_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,
        "translate build processing failed"),
    TRANSLATION_PARSE_NO_TEXT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR,
        "cannot parse the text for translate"),
    //rate limit
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "too many request"),
    // smtp email
    EXPIRED_VERIFICATION(HttpStatus.UNAUTHORIZED, "expired email-verification"),
    INVALID_SECURITY_CODE(HttpStatus.UNAUTHORIZED, "invalid security code"),

    // application
    INVALID_TOKEN_FORMED(HttpStatus.UNAUTHORIZED, "not support to this token`s formed"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "token is expired"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "token signature is invalid"),
    NOT_BEARER_TOKEN(HttpStatus.UNAUTHORIZED, "token is not start with BEARER or null"),
    DUPLICATED_USERNAME(HttpStatus.CONFLICT, "username is duplicated"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "email is duplicated"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "invalid password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user not found"),
    POST_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "post does not exist"),
    COMMENT_DOES_NOT_EXIST(HttpStatus.NOT_FOUND, "comment does not exist"),
    PAGE_SIZE_NOT_APPLICABLE(HttpStatus.BAD_REQUEST, "page size is too large"),
    CANNOT_FOLLOW_YOURSELF(HttpStatus.CONFLICT, "cannot follow yourself"),
    CANNOT_REPORT_YOURSELF(HttpStatus.CONFLICT, "cannot report yourself"),
    HAS_NOT_PERMISSION_TO_ACCESS(HttpStatus.UNAUTHORIZED, "has not permission to access"),
    HAS_NOT_AUTHENTICATION(HttpStatus.UNAUTHORIZED,"please verify your identity."),
    NO_SUCH_ALGORITHM(HttpStatus.INTERNAL_SERVER_ERROR, "no such algorithm"),
    NONE_MATCH(HttpStatus.NOT_FOUND, "can not find any match"),
    USER_BANNED(HttpStatus.BAD_REQUEST, "Your account is currently banned and unavailable."),
    RESTRICTED_CONTENT(HttpStatus.BAD_REQUEST, "already restricted"),

    NOT_SUPPORT_FORMAT(HttpStatus.BAD_REQUEST, "not support to this format"),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST,
        "Image upload failed, the image you selected may be in an unsupported format"),
    FAILED_RESIZE_IMAGE(HttpStatus.BAD_REQUEST, "image resize failed"),
    NOT_EXIST_ACTIVITY(HttpStatus.BAD_REQUEST, "not exist activity"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "invalid file type");


    private final HttpStatus httpStatus;
    private final String message;
}
