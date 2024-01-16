package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAll(Pageable pageable);
    Optional<PostEntity> findByPid(Long pid);
    Page<PostEntity> findByTitleContaining(String title,Pageable pageable);
    Page<PostEntity> findByUsernameContaining(String username,Pageable pageable);
    Page<PostEntity> findByUid(UserEntity uid,Pageable pageable);

}
