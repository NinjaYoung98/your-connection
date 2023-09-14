package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.post.PostCountEntity;
import com.sns.yourconnection.model.entity.post.PostEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostCountRepository extends JpaRepository<PostCountEntity, Long> {

    Optional<PostCountEntity> findByPostAndUser(PostEntity postEntity, UserEntity userEntity);

}
