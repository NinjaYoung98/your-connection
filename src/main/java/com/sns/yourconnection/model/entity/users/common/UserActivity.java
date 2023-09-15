package com.sns.yourconnection.model.entity.users.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserActivity {

    NORMAL("일반 유저"),
    ACTION_REQUIRED("관리자의 조치가 필요한 유저"),
    BAN("이용 제한 유저");

    private final String description;
}
