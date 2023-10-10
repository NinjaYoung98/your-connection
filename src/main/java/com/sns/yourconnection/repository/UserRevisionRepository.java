package com.sns.yourconnection.repository;

import com.sns.yourconnection.model.entity.users.UserActivity;
import com.sns.yourconnection.model.entity.users.UserEntity;
import com.sns.yourconnection.utils.loader.ClassUtil;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRevisionRepository {

    private final EntityManager entityManager;

    public UserActivity findPreviousActivityByUserId(Long userId) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        UserEntity userEntity = ClassUtil.castingInstance(
            auditReader.createQuery()
                .forRevisionsOfEntity(
                    UserEntity.class, true, true)
                .add(AuditEntity.id().eq(userId))
                .add(AuditEntity.property("userActivity").ne(UserActivity.LOCKED))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(1)   // 가장 최근의 값을 가져오기 위함
                .getSingleResult(),
            UserEntity.class
        );

        return userEntity.getUserActivity();
    }
}
