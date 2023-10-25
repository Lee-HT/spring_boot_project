package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    CommentEntity findByCid(Long cid);
    Page<CommentEntity> findByUid(UserEntity uid, Pageable pageable);
    Page<CommentEntity> findByPid(PostEntity pid, Pageable pageable);
}
