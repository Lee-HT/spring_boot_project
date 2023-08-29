package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Identifier.UidCid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, UidCid> {
    List<CommentLikeEntity> findByUid(UserEntity uid);
    int countByCid(CommentEntity cid);
}
