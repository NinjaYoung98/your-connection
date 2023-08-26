package com.sns.yourconnection.model.result.translate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TranslateResponse {

    private String text;

    public static TranslateResponse of(String text) {
        return new TranslateResponse(text);
    }
}
