package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUid(Long uid);
    UserEntity findByProvider(String provider);
    Page<UserEntity> findByUsernameContaining(String name, Pageable pageable);

}
