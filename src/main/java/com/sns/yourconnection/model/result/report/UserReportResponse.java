package com.sns.yourconnection.model.result.report;

import com.sns.yourconnection.model.dto.UserReport;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserReportResponse {

    private Long reporterId;
    private Long reportedUserId;
    private String content;
    private LocalDateTime reportedAt;

    public static UserReportResponse fromUserReport(UserReport userReport) {
        return new UserReportResponse(
            userReport.getReporter().getId(),
            userReport.getReportedUser().getId(),
            userReport.getContent(),
            userReport.getReportedAt()
        );
    }
}
