package com.sns.yourconnection.model.post.entity;

import com.sns.yourconnection.model.audit.LogAuditEntity;
import com.sns.yourconnection.model.user.dto.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

/*
    post update log를 남기기위한 entity
 */
@Entity
@Getter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"post_log\"")
public class PostLogEntity extends LogAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(name = "before_title")
    private String beforeTitle;

    @Column(name = "before_content", columnDefinition = "text")
    private String beforeContent;

    @Column(name = "after_title")
    private String afterTitle;

    @Column(name = "after_content", columnDefinition = "text")
    private String afterContent;

    private PostLogEntity(PostEntity post, String beforeTitle, String beforeContent,
        LocalDateTime beforeUpdatedAt, String beforeUpdatedBy) {
        this.post = post;
        this.beforeTitle = beforeTitle;
        this.beforeContent = beforeContent;
        this.beforeUpdatedAt = beforeUpdatedAt;
        this.beforeUpdatedBy = beforeUpdatedBy;
    }

    public static PostLogEntity of(PostEntity post, String beforeTitle, String beforeContent,
        LocalDateTime beforeUpdatedAt, String beforeUpdatedBy) {
        return new PostLogEntity(
            post, beforeTitle, beforeContent, beforeUpdatedAt, beforeUpdatedBy
        );
    }

    public void updateAndLog(String title, String content, PostEntity post) {
        this.afterTitle = title;
        this.afterContent = content;
        post.updateAndLog(this);
    }
}
