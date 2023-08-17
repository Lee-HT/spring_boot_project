package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PostRepositoryTest {

    private final PostRepository postRepository;
    private static List<PostEntity> posts;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @BeforeAll
    static void setPost() {
        posts = new ArrayList<>();
        posts.add(PostEntity.builder().title("title1").contents("contents1").username("user1")
                .build());
        posts.add(PostEntity.builder().title("title2").contents("contents2").username("user2")
                .build());
    }

    @BeforeEach
    public void savePosts() {
        postRepository.saveAll(posts);
    }

    @Test
    public void findAll() {
        List<PostEntity> posts = postRepository.findAll();
    }

    @Test
    public void findByUsername() {
        String username = "user";
        List<PostEntity> posts = postRepository.findByUsername(username);
    }

    @Test
    public void findByTitle() {
        String title = "title";
        List<PostEntity> posts = postRepository.findByTitle(title);
    }

    @Test
    public void saveAll() {
        List<PostEntity> newPosts = new ArrayList<>();
        newPosts.add(PostEntity.builder().title("title3").contents("contents3").username("user3")
                .build());
        newPosts.add(PostEntity.builder().title("title4").contents("contents4").username("user4")
                .build());
        postRepository.saveAll(newPosts);
    }

    @Test
    public void deleteAll() {
        postRepository.deleteAll(posts);
    }

}