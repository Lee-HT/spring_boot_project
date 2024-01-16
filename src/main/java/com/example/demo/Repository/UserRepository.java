package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUid(Long uid);
    Optional<UserEntity> findByProvider(String provider);
    Page<UserEntity> findByUsernameContaining(String name, Pageable pageable);

}
