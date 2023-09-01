package com.sns.yourconnection.controller.response;

import com.sns.yourconnection.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseError {

    private String id;
    private String message;

    public static ResponseError response(String id, String message) {
        return new ResponseError(id, message);
    }

    public static ResponseError response(ErrorCode errorCode) {
        return new ResponseError(errorCode.getHttpStatus().toString(), errorCode.getMessage());
    }

    public static ResponseError response(ErrorCode errorCode, String message) {
        return new ResponseError(errorCode.getHttpStatus().toString(), message);
    }
}
