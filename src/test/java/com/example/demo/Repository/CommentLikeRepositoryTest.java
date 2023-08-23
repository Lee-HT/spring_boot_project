package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentLikeRepositoryTest {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private List<CommentLikeEntity> commentLikes = new ArrayList<>();
    private List<CommentEntity> comments = new ArrayList<>();
    private UserEntity uid;

    @Autowired
    public CommentLikeRepositoryTest(CommentLikeRepository commentLikeRepository,
            CommentRepository commentRepository, UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.uid = UserEntity.builder().username("user1").email("email1@gmail.com").build();
        setObjects();
    }

    void setObjects() {
        CommentEntity cid1 = CommentEntity.builder().contents("contents1").build();
        CommentEntity cid2 = CommentEntity.builder().contents("contents2").build();
        comments.add(cid1);
        comments.add(cid2);
        commentLikes.add(CommentLikeEntity.builder()
                .uid(uid).cid(cid1).good(true).hate(false).build());
        commentLikes.add(CommentLikeEntity.builder()
                .uid(uid).cid(cid2).good(false).hate(true).build());
    }
     @BeforeEach
     public void setCommentLikes(){
        userRepository.save(uid);
        commentRepository.saveAll(comments);
        commentLikeRepository.saveAll(commentLikes);
     }

    @Test
    public void findAll() {
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findAll();

        System.out.println("======== findAll ========");
        System.out.println(commentLikes);
    }

    @Test
    public void findByUid() {
        UserEntity userEntity = UserEntity.builder().uid(1L).build();
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findByUid(userEntity);

        System.out.println("======== findByUid ========");
        System.out.println(commentLikes);
    }

    @Test
    public void saveAll() {
        List<CommentEntity> comments = new ArrayList<>();
        List<CommentLikeEntity> commentLikes = new ArrayList<>();

        UserEntity uid = UserEntity.builder().username("user2").email("email2@gmail.com").build();
        CommentEntity cid1 = CommentEntity.builder().contents("contents1").build();
        CommentEntity cid2 = CommentEntity.builder().contents("contents2").build();
        comments.add(cid1);
        comments.add(cid2);
        commentLikes.add(CommentLikeEntity.builder().uid(uid).cid(cid1).good(true).hate(false).build());
        commentLikes.add(CommentLikeEntity.builder().uid(uid).cid(cid2).good(false).hate(true).build());

        userRepository.save(uid);
        commentRepository.saveAll(comments);
        commentLikeRepository.saveAll(commentLikes);
        List<CommentLikeEntity> result = commentLikeRepository.findAll();

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

    @Test
    public void countByCid() {
        CommentEntity commentEntity = CommentEntity.builder().cid(1L).build();
        Long countLikes = commentLikeRepository.countByCid(commentEntity);

        System.out.println("======== countByCid ========");
        System.out.println(countLikes);
    }

}