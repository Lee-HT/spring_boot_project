package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAll(Pageable pageable);
    PostEntity findByPid(Long pid);
    Page<PostEntity> findByTitleContaining(String title,Pageable pageable);
    Page<PostEntity> findByUsername(UserEntity username,Pageable pageable);

}
