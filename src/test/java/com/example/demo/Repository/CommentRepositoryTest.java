package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.UserEntity;
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

@ActiveProfiles(profiles = "local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private List<CommentEntity> comments = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setComments() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com").build());
        comments.add(CommentEntity.builder().username(users.get(0)).contents("contents1").build());
        comments.add(CommentEntity.builder().username(users.get(0)).contents("contents2").build());

        userRepository.saveAll(users);
        commentRepository.saveAll(comments);
    }

    @Test
    public void findAll() {
        List<CommentEntity> comments = commentRepository.findAll();

        Assertions.assertThat(comments).usingRecursiveComparison().isEqualTo(this.comments);

        System.out.println("======== findAll ========");
        System.out.println(comments);
    }

    @Test
    public void findByUsername() {
        UserEntity user = UserEntity.builder().username("user1").email("email1@gmail.com")
                .build();
        List<CommentEntity> comments = commentRepository.findByUsername(user);

        Assertions.assertThat(comments).usingRecursiveComparison().isEqualTo(this.comments);

        System.out.println("======== findByUid ========");
        System.out.println(comments);
    }

    @Test
    public void saveAll() {
        List<CommentEntity> comments = new ArrayList<>();
        comments.add(CommentEntity.builder().username(users.get(1)).contents("contents3")
                .build());
        comments.add(CommentEntity.builder().username(users.get(1)).contents("contents4")
                .build());
        List<CommentEntity> result = commentRepository.saveAll(comments);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(comments);

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

}