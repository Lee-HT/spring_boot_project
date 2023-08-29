package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    PostEntity findByPid(Long pid);
    List<PostEntity> findByTitleContaining(String title);
    List<PostEntity> findByUsername(UserEntity username);

}
