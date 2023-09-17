package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.report.PostReportEntity;
import com.sns.yourconnection.model.entity.report.UserReportEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReportRepository extends JpaRepository<PostReportEntity, Long> {

    Integer countByReportedPost(PostEntity reportedPost);

    boolean existsByReporterAndReportedPost(UserEntity reporter, PostEntity reportedPost);

    Page<PostReportEntity> findByReportedPost(PostEntity reportedPost, Pageable pageable);
}
