package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    // commentLikes PK 가 null 값이 아님으로 Detached 상태로 판단 -> merge 수행
    @BeforeEach
    void setCommentLikes() {
        for (int i = 1; i < 4; i++) {
            users.add(UserEntity.builder().username("user" + i).email("email" + i + "@gmail.com")
                    .provider("google_" + i).build());
        }
        for (int i = 1; i < 6; i++) {
            comments.add(CommentEntity.builder().uid(users.get(i / 2)).contents("contents" + i)
                    .build());
        }
        for (int i = 0; i < 5; i++) {
            commentLikes.add(CommentLikeEntity.builder().uid(users.get(i / 3)).cid(comments.get(i))
                    .likes(i % 2 == 0).build());
        }

        userRepository.saveAll(users);
        commentRepository.saveAll(comments);
        this.commentLikes = commentLikeRepository.saveAll(commentLikes);
    }

    @Test
    void findAll() {
        List<CommentLikeEntity> commentLikes = commentLikeRepository.findAll();

        Assertions.assertThat(commentLikes).usingRecursiveComparison()
                .isEqualTo(this.commentLikes);
    }

    @Test
    void findByUid() {
        List<CommentLikeEntity> result = commentLikeRepository.findByUid(users.get(0));
        List<CommentLikeEntity> commentLikes = Arrays.asList(this.commentLikes.get(0), this.commentLikes.get(1),
                this.commentLikes.get(2));

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(commentLikes);
    }

    @Test
    void findByCid() {
        List<CommentLikeEntity> result = commentLikeRepository.findByCid(comments.get(0));
        List<CommentLikeEntity> commentLikes = Collections.singletonList(this.commentLikes.get(0));

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(commentLikes);
    }

    @Test
    void saveAll() {
        List<CommentLikeEntity> commentLikes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            commentLikes.add(CommentLikeEntity.builder().uid(users.get(2)).cid(comments.get(i))
                    .likes(i % 2 == 0).build());
        }
        List<CommentLikeEntity> result = commentLikeRepository.saveAll(commentLikes);

        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt").isEqualTo(commentLikes);
    }

    @Test
    void countByCidAndLikes() {
        Long countLikes = commentLikeRepository.countByCidAndLikes(comments.get(0), true);

        Assertions.assertThat(countLikes).usingRecursiveComparison().isEqualTo(1L);
    }

}