package com.sns.yourconnection.model.param.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmtpVerifyRequest {
    private String securityCode;
}
