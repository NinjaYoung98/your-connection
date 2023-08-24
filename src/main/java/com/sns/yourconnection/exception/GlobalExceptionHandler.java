package com.sns.yourconnection.exception;

import com.sns.yourconnection.controller.response.ResponseError;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> globalExceptionHandler(Exception e) {
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

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseError> appExceptionHandler(AppException e) {
        log.warn("[AppException Occurs] message: {} HttpStatus: {}", e.getErrorCode().getMessage(), e.getErrorCode().getHttpStatus());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResponseError.response(e.getErrorCode()));
    }
}
