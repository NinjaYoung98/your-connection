package com.sns.yourconnection.model.entity.audit;

import lombok.Getter;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class LogAuditEntity {

    @Column(name = "before_updated_at")
    protected LocalDateTime beforeUpdatedAt;

    @Column(name = "before_updated_by")
    protected String beforeUpdatedBy;

    @LastModifiedDate
    @Column(name = "after_updated_at")
    protected LocalDateTime afterUpdatedAt;

    @LastModifiedBy
    @Column(name = "after_updated_by")
    protected String afterUpdatedBy;
}
