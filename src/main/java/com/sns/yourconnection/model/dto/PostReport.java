package com.sns.yourconnection.model.dto;

import com.sns.yourconnection.model.entity.report.PostReportEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReport {

    private User reporter;
    private Post reportedPost;
    private String content;
    private LocalDateTime reportedAt;

    public static PostReport fromEntity(PostReportEntity postReportEntity) {
        return new PostReport(
            User.fromEntity(postReportEntity.getReporter()),
            Post.fromEntity(postReportEntity.getReportedPost()),
            postReportEntity.getContent(),
            postReportEntity.getReportedAt()
        );
    }
}
