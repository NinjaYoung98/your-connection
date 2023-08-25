package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.post.entity.PostCountEntity;
import com.sns.yourconnection.model.post.entity.PostEntity;
import com.sns.yourconnection.model.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostCountRepository extends JpaRepository<PostCountEntity, Long> {

    Optional<PostCountEntity> findByPostAndUser(PostEntity postEntity, UserEntity userEntity);

}
