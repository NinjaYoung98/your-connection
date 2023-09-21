package com.sns.yourconnection.model.entity.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailVerified {
    VERIFIED(true),
    UNVERIFIED(false);

    private boolean isVerified;
}
