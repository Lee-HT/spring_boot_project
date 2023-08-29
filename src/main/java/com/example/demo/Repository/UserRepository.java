package com.example.demo.Repository;

import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUid(Long uid);
    UserEntity findByUsername(String name);
    List<UserEntity> findByUsernameContaining(String name);

}
