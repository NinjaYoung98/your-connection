package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.report.UserReportEntity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReportEntity, Long> {

    Integer countByReportedUser(UserEntity reportedUser);

    boolean existsByReporterAndReportedUser(UserEntity reporter, UserEntity reportedUser);

    Page<UserReportEntity> findByReportedUser(UserEntity reportedUser, Pageable pageable);
}
