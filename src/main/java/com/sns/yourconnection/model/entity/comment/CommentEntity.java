package com.sns.yourconnection.model.entity.comment;

import com.sns.yourconnection.model.entity.audit.AuditEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.user.UserEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@Table(name = "\"comment\"")
public class CommentEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private CommentEntity(String comment, UserEntity user, PostEntity post) {
        this.comment = comment;
        this.user = user;
        this.post = post;
    }

    public static CommentEntity of(String comment, UserEntity user, PostEntity post) {
        return new CommentEntity(comment, user, post);
    }

    public void update(String comment) {
        this.comment = comment;
    }
}
