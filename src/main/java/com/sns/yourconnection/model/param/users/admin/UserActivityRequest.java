package com.sns.yourconnection.model.param.users.admin;

import com.sns.yourconnection.model.entity.users.common.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserActivityRequest {

    private UserActivity userActivity;
}
