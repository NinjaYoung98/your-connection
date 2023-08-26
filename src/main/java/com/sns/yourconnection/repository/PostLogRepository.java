package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLogRepository extends JpaRepository<PostLogEntity, Long> {
}
