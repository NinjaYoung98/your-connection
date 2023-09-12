package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.post.PostStorageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostStorageRepository extends JpaRepository<PostStorageEntity, Long> {

    List<PostStorageEntity> findByPost(PostEntity postEntity);
}
