package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.comment.entity.CommentEntity;
import com.sns.yourconnection.model.post.entity.PostEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
