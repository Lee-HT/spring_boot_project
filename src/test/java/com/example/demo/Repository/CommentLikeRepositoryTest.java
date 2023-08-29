package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class CommentLikeRepositoryTest {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private List<CommentLikeEntity> commentLikes = new ArrayList<>();
    private List<CommentEntity> comments = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();

    @Autowired
    public CommentLikeRepositoryTest(CommentLikeRepository commentLikeRepository,
            CommentRepository commentRepository, UserRepository userRepository) {
        this.commentLikeRepository = commentLikeRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    public void setCommentLikes() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com").build());
        comments.add(CommentEntity.builder().contents("contents1").build());
        comments.add(CommentEntity.builder().contents("contents2").build());
        commentLikes.add(
                CommentLikeEntity.builder().uid(users.get(0)).cid(comments.get(0)).good(true)
                        .hate(false).build());
        commentLikes.add(
                CommentLikeEntity.builder().uid(users.get(0)).cid(comments.get(1)).good(false)
                        .hate(true).build());

        userRepository.saveAll(users);
        commentRepository.saveAll(comments);
        commentLikeRepository.saveAll(commentLikes);
    }

    @Test
    public void findAll() {
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findAll();

        Assertions.assertThat(commentLikes).usingRecursiveComparison().isEqualTo(this.commentLikes);

        System.out.println("======== findAll ========");
        System.out.println(commentLikes);
    }

    @Test
    public void findByUid() {
        UserEntity userEntity = UserEntity.builder().uid(1L).build();
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findByUid(userEntity);

        Assertions.assertThat(commentLikes).usingRecursiveComparison().isEqualTo(this.commentLikes);

        System.out.println("======== findByUid ========");
        System.out.println(commentLikes);
    }

    @Test
    public void saveAll() {
        List<CommentLikeEntity> commentLikes = new ArrayList<>();
        commentLikes.add(
                CommentLikeEntity.builder().uid(users.get(1)).cid(comments.get(0)).good(true)
                        .hate(false).build());
        commentLikes.add(
                CommentLikeEntity.builder().uid(users.get(1)).cid(comments.get(1)).good(false)
                        .hate(true).build());
        List<CommentLikeEntity> result = commentLikeRepository.saveAll(commentLikes);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(commentLikes);

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

    @Test
    public void countByCid() {
        CommentEntity commentEntity = CommentEntity.builder().cid(1L).build();
        int countLikes = commentLikeRepository.countByCid(commentEntity);

        Assertions.assertThat(countLikes).usingRecursiveComparison().isEqualTo(1);

        System.out.println("======== countByCid ========");
        System.out.println(countLikes);
    }

}