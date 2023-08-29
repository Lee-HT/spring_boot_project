package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Identifier.UidPid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, UidPid> {
    List<PostLikeEntity> findByUid(UserEntity uid);
    int countByPid(PostEntity pid);
}
