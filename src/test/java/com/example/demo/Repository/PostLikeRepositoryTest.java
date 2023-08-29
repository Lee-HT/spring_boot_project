package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
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

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
public class PostLikeRepositoryTest {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private List<PostLikeEntity> postLikes = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();

    @Autowired
    public PostLikeRepositoryTest(PostLikeRepository postLikeRepository,
            PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
    }

    @BeforeAll
    void setPostLikes() {
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com").build());
        posts.add(PostEntity.builder().title("title1").contents("contents1").build());
        posts.add(PostEntity.builder().title("title2").contents("contents2").build());
        postLikes.add(
                PostLikeEntity.builder().uid(users.get(0)).pid(posts.get(0)).good(true).hate(false)
                        .build());
        postLikes.add(
                PostLikeEntity.builder().uid(users.get(0)).pid(posts.get(1)).good(false).hate(true)
                        .build());

        userRepository.saveAll(users);
        postRepository.saveAll(posts);
        postLikeRepository.saveAll(postLikes);
    }

    @Test
    public void findAll() {
        List<PostLikeEntity> postLikes = postLikeRepository.findAll();

        Assertions.assertThat(postLikes).usingRecursiveComparison().isEqualTo(this.postLikes);

        System.out.println("======== findAll ========");
        System.out.println(postLikes);
    }

    @Test
    public void findByUid() {
        UserEntity uid = UserEntity.builder().uid(1L).build();
        List<PostLikeEntity> postLikes = postLikeRepository.findByUid(uid);

        Assertions.assertThat(postLikes).usingRecursiveComparison().isEqualTo(this.postLikes);

        System.out.println("======== findByUid ========");
        System.out.println(postLikes);
    }

    @Test
    public void saveAll() {
        List<PostLikeEntity> postLikes = new ArrayList<>();
        postLikes.add(
                PostLikeEntity.builder().uid(users.get(1)).pid(posts.get(0)).good(true).hate(false)
                        .build());
        postLikes.add(
                PostLikeEntity.builder().uid(users.get(1)).pid(posts.get(1)).good(false).hate(true)
                        .build());
        List<PostLikeEntity> result = postLikeRepository.saveAll(postLikes);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(postLikes);

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

    @Test
    public void countByPid() {
        int countLikes = postLikeRepository.countByPid(posts.get(0));

        Assertions.assertThat(countLikes).isEqualTo(1);

        System.out.println("======== countByPid ========");
        System.out.println(countLikes);
    }
}
