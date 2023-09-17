package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.report.ContentActivity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT p FROM PostEntity p JOIN FETCH p.postCounts WHERE p.id = :postId")
    Optional<PostEntity> fetchJoinIdWithPostCount(@Param("postId") Long postId);

    @Query("SELECT p FROM PostEntity p WHERE (:keyword IS NULL OR :keyword = '') OR p.title LIKE %:keyword%")
    Page<PostEntity> searchByKeyword(@Param(value = "keyword") String keyword, Pageable pageable);

    Page<PostEntity> findByContentActivity(ContentActivity contentActivity,Pageable pageable);
}
