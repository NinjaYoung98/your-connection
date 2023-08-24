package com.sns.yourconnection.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.naming.AuthenticationException;

@AllArgsConstructor
@Getter
public class MissingBearerTokenException extends AuthenticationException {
    private ErrorCode errorCode;
}
