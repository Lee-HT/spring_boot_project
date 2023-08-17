package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import jakarta.annotation.Nonnull;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Nonnull
    List<PostEntity> findByUsername(String username);

    @Nonnull
    List<PostEntity> findByTitle(String title);

}
