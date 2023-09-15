package com.sns.yourconnection.model.entity.report;

import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post_report SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@Table(name = "\"post_report\"")
public class PostReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private UserEntity reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_post_id")
    private PostEntity reportedPost;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PostPersist
    private void setCreatedAt() {
        reportedAt = LocalDateTime.now();
    }

    private PostReportEntity(UserEntity reporter, PostEntity reportedPost, String content) {
        this.reporter = reporter;
        this.reportedPost = reportedPost;
        this.content = content;
    }

    public static PostReportEntity of(UserEntity reporter, PostEntity reportedPost,
        String content) {
        return new PostReportEntity(reporter, reportedPost, content);
    }
}
