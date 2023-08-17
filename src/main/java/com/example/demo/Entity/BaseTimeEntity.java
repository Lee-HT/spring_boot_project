package com.example.demo.Entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    // 엔티티 생성시 시간 자동 저장
    @CreatedDate
    private LocalDateTime createAt;
    // Entity 변경시 시간 자동 저장
    @LastModifiedDate
    private LocalDateTime updateAt;
}
