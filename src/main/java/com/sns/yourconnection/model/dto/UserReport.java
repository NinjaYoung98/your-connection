package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.report.UserReportEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReport {

    private User reporter;
    private User reportedUser;
    private String content;
    private LocalDateTime reportedAt;

    public static UserReport fromEntity(UserReportEntity userReportEntity) {
        return new UserReport(
            User.fromEntity(userReportEntity.getReporter()),
            User.fromEntity(userReportEntity.getReportedUser()),
            userReportEntity.getContent(),
            userReportEntity.getReportedAt()
        );
    }
}
