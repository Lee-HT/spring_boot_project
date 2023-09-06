package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
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

    @BeforeEach
    public void setCommentLikes() {
        for (int i = 1; i < 4; i++) {
            users.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com")
                    .build());
        }
        for (int i = 1; i < 6; i++) {
            comments.add(CommentEntity.builder().username(users.get(i / 2)).contents("contents" + i)
                    .build());
        }
        for (int i = 0; i < 5; i++) {
            commentLikes.add(
                    CommentLikeEntity.builder().uid(users.get(i / 3)).cid(comments.get(i))
                            .likes(i % 2 == 0).hate(i % 2 != 0).build());
        }
        System.out.println(users);

        userRepository.saveAll(users);
        commentRepository.saveAll(comments);
        commentLikeRepository.saveAll(commentLikes);
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        System.out.println("======== findAll ========");
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findAll();

        Assertions.assertThat(commentLikes).usingRecursiveComparison().isEqualTo(this.commentLikes);

        System.out.println(commentLikes);
    }

//    @Test
//    @DisplayName("UID 기준 SELECT")
//    public void findByUid() {
//        System.out.println("======== findByUid ========");
//        UserEntity userEntity = UserEntity.builder().uid(1L).build();
//        List<CommentLikeEntity> commentLikes = commentLikeRepository.findByUid(userEntity);
//
//        Assertions.assertThat(commentLikes).usingRecursiveComparison()
//                .isEqualTo(this.commentLikes.subList(0, 3));
//
//        System.out.println(commentLikes);
//    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        System.out.println("======== saveAll ========");
        List<CommentLikeEntity> commentLikes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            commentLikes.add(
                    CommentLikeEntity.builder().uid(users.get(2)).cid(comments.get(i))
                            .likes(i % 2 == 0)
                            .hate(i % 2 != 0).build());
        }
        List<CommentLikeEntity> result = commentLikeRepository.saveAll(commentLikes);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(commentLikes);

        System.out.println(result);
    }

    @Test
    @DisplayName("CID COUNT SELECT")
    public void countByCid() {
        System.out.println("======== countByCid ========");
        CommentEntity commentEntity = CommentEntity.builder().cid(1L).build();
        int countLikes = commentLikeRepository.countByCid(commentEntity);

        Assertions.assertThat(countLikes).usingRecursiveComparison().isEqualTo(1);

        System.out.println(countLikes);
    }

}