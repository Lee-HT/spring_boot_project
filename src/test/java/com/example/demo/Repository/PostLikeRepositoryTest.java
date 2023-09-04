package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
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

@ActiveProfiles("test")
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
        for (int i = 1; i < 4; i++) {
            users.add(UserEntity.builder().uid((long) i).username("user" + i)
                    .email("email" + i + "@gmail.com").build());
        }
        for (int i = 1; i < 6; i++) {
            posts.add(PostEntity.builder().pid((long) i).title("title" + i).contents("contents" + i)
                    .category("category" + i).build());
        }
        for (int i = 0; i < 5; i++) {
            postLikes.add(
                    PostLikeEntity.builder().uid(users.get(i / 3)).pid(posts.get(i))
                            .likes(i % 2 == 0).hate(i % 2 != 0).build());
        }

        userRepository.saveAll(users);
        postRepository.saveAll(posts);
        postLikeRepository.saveAll(postLikes);
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        List<PostLikeEntity> postLikes = postLikeRepository.findAll();

        Assertions.assertThat(postLikes).usingRecursiveComparison().isEqualTo(this.postLikes);

        System.out.println("======== findAll ========");
        System.out.println(postLikes);
    }

    @Test
    @DisplayName("UID 기준 SELECT")
    public void findByUid() {
        UserEntity uid = UserEntity.builder().uid(1L).build();
        List<PostLikeEntity> postLikes = postLikeRepository.findByUid(uid);

        Assertions.assertThat(postLikes).usingRecursiveComparison()
                .isEqualTo(this.postLikes.subList(0, 3));

        System.out.println("======== findByUid ========");
        System.out.println(postLikes);
    }

    @Test
    @DisplayName("PID,UID 기준 SELECT")
    public void findByPidAndUid() {
        PostLikeEntity postLike = postLikeRepository.findByPidAndUid(posts.get(0), users.get(0));
    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        List<PostLikeEntity> postLikes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            postLikes.add(
                    PostLikeEntity.builder().uid(users.get(2)).pid(posts.get(i)).likes(i % 2 == 0)
                            .hate(i % 2 != 0).build());
        }
        List<PostLikeEntity> result = postLikeRepository.saveAll(postLikes);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(postLikes);

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

    @Test
    @DisplayName("PID COUNT SELECT")
    public void countByPid() {
        int countLikes = postLikeRepository.countByPid(posts.get(0));

        Assertions.assertThat(countLikes).isEqualTo(1);

        System.out.println("======== countByPid ========");
        System.out.println(countLikes);
    }
}
