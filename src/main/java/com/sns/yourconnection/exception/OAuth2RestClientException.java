package com.sns.yourconnection.exception;

import org.springframework.web.client.RestClientException;

public class OAuth2RestClientException extends RestClientException {

    public OAuth2RestClientException(String msg) {
        super(msg);
    }

    public OAuth2RestClientException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
