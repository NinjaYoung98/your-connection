package com.sns.yourconnection.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ResponseSuccess<T> {

    private T data;

    // Success with data only
    public static ResponseSuccess<Void> response() {
        return new ResponseSuccess<>(null);
    }

    // Success with data and page info
    public static <T> ResponseSuccess<T> response(T data) {
        return new ResponseSuccess(data);
    }
}
