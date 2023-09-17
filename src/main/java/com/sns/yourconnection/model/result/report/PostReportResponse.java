package com.sns.yourconnection.model.result.report;

import com.sns.yourconnection.model.dto.PostReport;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReportResponse {

    private Long reporterId;
    private Long reportedPostId;
    private String content;
    private LocalDateTime reportedAt;

    public static PostReportResponse fromPostReport(PostReport postReport) {
        return new PostReportResponse(
            postReport.getReporter().getId(),
            postReport.getReportedPost().getId(),
            postReport.getContent(),
            postReport.getReportedAt()
        );
    }
}
