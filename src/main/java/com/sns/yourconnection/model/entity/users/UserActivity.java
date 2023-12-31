package com.sns.yourconnection.model.entity.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivity {

    NORMAL("일반 유저"),
    FLAGGED("신고받은 유저"),
    LOCKED("일시정지 "),
    BAN("이용 제한 유저");

    private final String description;
}
