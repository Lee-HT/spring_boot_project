package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private List<CommentEntity> comments = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    @Autowired
    private PostRepository postRepository;

    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository,
            UserRepository userRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setComments() {
        for (int i = 1; i < 4; i++) {
            users.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com").build());
        }
        for (int i = 1; i < 4; i++) {
            posts.add(PostEntity.builder().username(users.get(i)).title("title" + i)
                    .contents("contents" + i).category("category1").build());
        }
        for (int i = 0; i < 4; i++) {
            comments.add(CommentEntity.builder().username(users.get(i / 2))
                    .contents("contents" + (i + 1)).build());
        }

        postRepository.saveAll(posts);
        userRepository.saveAll(users);
        commentRepository.saveAll(comments);
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        System.out.println("======== findAll ========");
        List<CommentEntity> comments = commentRepository.findAll();

        Assertions.assertThat(comments).usingRecursiveComparison().isEqualTo(this.comments);

        System.out.println(comments);
    }

    @Test
    @DisplayName("USERNAME 기준 SELECT")
    public void findByUsername() {
        System.out.println("======== findByUid ========");
        UserEntity user = UserEntity.builder().username("user1").email("email1@gmail.com")
                .build();
        List<CommentEntity> comments = commentRepository.findByUsername(user);

        Assertions.assertThat(comments).usingRecursiveComparison()
                .isEqualTo(this.comments.subList(0, 2));

        System.out.println(comments);
    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        System.out.println("======== saveAll ========");
        List<CommentEntity> comments = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            comments.add(CommentEntity.builder().username(users.get(2)).contents("contents" + i)
                    .build());
        }
        List<CommentEntity> result = commentRepository.saveAll(comments);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(comments);

        System.out.println(result);
    }

}