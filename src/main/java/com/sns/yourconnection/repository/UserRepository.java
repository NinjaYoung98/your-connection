package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.model.entity.users.UserActivity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findByEmail(String email);

    Page<UserEntity> findByUserActivity(UserActivity userActivity, Pageable pageable);
}
