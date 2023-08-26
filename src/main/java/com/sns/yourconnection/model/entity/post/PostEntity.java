package com.sns.yourconnection.model.entity.post;

import com.sns.yourconnection.model.entity.audit.AuditEntity;
import com.sns.yourconnection.model.entity.user.UserEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@Table(name = "\"post\"")
public class PostEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    private List<PostCountEntity> postCounts = new ArrayList<>();

    private PostEntity(String title, String content, UserEntity user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static PostEntity of(String title, String content, UserEntity user) {
        return new PostEntity(title, content, user);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void updateAndLog(PostLogEntity postLog) {
        this.title = postLog.getAfterTitle();
        this.content = postLog.getAfterContent();
    }
}