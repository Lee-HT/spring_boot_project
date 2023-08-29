package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
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
class PostRepositoryTest {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private List<PostEntity> posts = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setPosts() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com").build());
        posts.add(PostEntity.builder().title("title1").contents("contents1").username(users.get(0))
                .build());
        posts.add(PostEntity.builder().title("title2").contents("contents2").username(users.get(0))
                .build());

        userRepository.saveAll(users);
        postRepository.saveAll(posts);
    }

    @Test
    public void findAll() {
        List<PostEntity> posts = postRepository.findAll();

        Assertions.assertThat(posts).usingRecursiveComparison().isEqualTo(this.posts);

        System.out.println("======== findAll ========");
        System.out.println(posts);
    }

    @Test
    public void findByUsername() {
//        UserEntity username = UserEntity.builder().username("user1").build();
        List<PostEntity> posts = postRepository.findByUsername(users.get(0));

        Assertions.assertThat(posts).usingRecursiveComparison().isEqualTo(this.posts);

        System.out.println("======== findByUsername ========");
        System.out.println(posts);
    }

    @Test
    public void findByTitleContaining() {
        String title = "title";
        List<PostEntity> posts = postRepository.findByTitleContaining(title);
        Assertions.assertThat(posts).usingRecursiveComparison()
                .isEqualTo(this.posts);

        System.out.println("======== findByTitleContaining ========");
        System.out.println(posts);
    }

    @Test
    public void saveAll() {
        List<PostEntity> Posts = new ArrayList<>();
        Posts.add(PostEntity.builder().title("title3").contents("contents3")
                .username(this.users.get(1)).build());
        Posts.add(PostEntity.builder().title("title4").contents("contents4")
                .username(this.users.get(1)).build());
        List<PostEntity> result = postRepository.saveAll(Posts);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(Posts);

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

}