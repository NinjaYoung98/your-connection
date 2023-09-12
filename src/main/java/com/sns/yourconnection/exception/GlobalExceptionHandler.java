package com.sns.yourconnection.exception;

import com.sns.yourconnection.controller.response.ResponseError;
import com.sns.yourconnection.service.notifitation.NotificationService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
/**
 * @ExceptionHandling
 * @apiNote Internal_server_Error (HttpStatus: 500) => return ResponseError and send telegram message.
 **/
public class GlobalExceptionHandler {

    private final NotificationService notificationService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> globalExceptionHandler(Exception e) {
        notificationService.sendMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        log.error("[InternalServerError Occurs] error: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
            .body(ResponseError.response(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> runtimeExceptionHandler(RuntimeException e) {
        log.error("[RuntimeException Occurs] error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(ResponseError.response(HttpStatus.CONFLICT.toString(), e.getMessage()));
    }

    @ExceptionHandler(TelegramException.class)
    public ResponseEntity<ResponseError> telegramExceptionHandler(TelegramException e) {
        log.error("[TelegramError Occurs] error: {}", e.getErrorCode().name());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(ResponseError.response(e.getErrorCode()));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ResponseError> restClientException(RestClientException e) {
        notificationService.sendMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        log.error("[RestClientException Occurs] error: {}", e.getMessage());
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
            .body(ResponseError.response(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TranslateException.class)
    public ResponseEntity<ResponseError> translateExceptionHandler(TranslateException e) {
        notificationService.sendMessage(e.getErrorCode().getMessage());
        log.error("[TranslateError Occurs] error: {}", e.getErrorCode().name());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(ResponseError.response(e.getErrorCode()));
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseError> appExceptionHandler(AppException e) {
        log.warn("[AppException Occurs] message: {} HttpStatus: {}", e.getErrorCode().getMessage(),
            e.getErrorCode().getHttpStatus());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(ResponseError.response(e.getErrorCode(),e.getMessage()));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ResponseError> malformedJwtExceptionHandler() {
        log.warn("[invalid token malformed] message: {}",
            ErrorCode.INVALID_TOKEN_FORMED.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_TOKEN_FORMED.getHttpStatus())
            .body(ResponseError.response(ErrorCode.INVALID_TOKEN_FORMED));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ResponseError> signatureJwtExceptionHandler() {
        log.warn("[Invalid token signature] message: {}",
            ErrorCode.INVALID_TOKEN_SIGNATURE.getMessage());
        return ResponseEntity.status(ErrorCode.INVALID_TOKEN_SIGNATURE.getHttpStatus())
            .body(ResponseError.response(ErrorCode.INVALID_TOKEN_SIGNATURE));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ResponseError> expiredJwtExceptionHandler() {
        log.warn("[expired token] message: {}", ErrorCode.EXPIRED_TOKEN.getMessage());
        return ResponseEntity.status(ErrorCode.EXPIRED_TOKEN.getHttpStatus())
            .body(ResponseError.response(ErrorCode.EXPIRED_TOKEN));
    }

    @ExceptionHandler(MissingBearerTokenException.class)
    public ResponseEntity<ResponseError> missingBearerTokenExceptionHandler(
        MissingBearerTokenException e) {
        log.warn("[Missing Bearer Token] message: {}", e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(ResponseError.response(e.getErrorCode()));
    }
}
