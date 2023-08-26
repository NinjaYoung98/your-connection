package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.comment.CommentEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPost(PostEntity postEntity, Pageable pageable);
}
