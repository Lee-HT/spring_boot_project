package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    // 엔티티 생성시 시간 자동 저장
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    // Entity 변경시 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
