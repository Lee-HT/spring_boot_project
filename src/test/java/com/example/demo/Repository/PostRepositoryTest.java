package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
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

@ActiveProfiles(profiles = "local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PostRepositoryTest {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private List<PostEntity> posts = new ArrayList<>();
    private UserEntity user1;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.user1 = UserEntity.builder().username("user1").email("email@gmail.com").build();
        setObjects();
    }

    void setObjects() {
        posts.add(PostEntity.builder().title("title1").contents("contents1").username(user1)
                .build());
        posts.add(PostEntity.builder().title("title2").contents("contents2").username(user1)
                .build());
    }
    @BeforeEach
    void setPosts(){
        userRepository.save(user1);
        postRepository.saveAll(posts);
    }

    @Test
    public void findAll() {
        List<PostEntity> posts = postRepository.findAll();

        System.out.println("======== findAll ========");
        System.out.println(posts);
    }

    @Test
    public void findByUsername() {
        UserEntity username = UserEntity.builder().username("user1").build();
        List<PostEntity> posts = postRepository.findByUsername(username);

        System.out.println("======== findByUsername ========");
        System.out.println(posts);
    }

    @Test
    public void findByTitle() {
        String title = "title1";
        List<PostEntity> posts = postRepository.findByTitle(title);

        System.out.println("======== findByTitle ========");
        System.out.println(posts);
    }

    @Test
    public void saveAll() {
        List<PostEntity> newPosts = new ArrayList<>();
        UserEntity user = UserEntity.builder().username("user2").email("email2@gmail.com").build();
        newPosts.add(PostEntity.builder().title("title3").contents("contents3").username(user).build());
        newPosts.add(PostEntity.builder().title("title4").contents("contents4").username(user).build());
        userRepository.save(user);
        postRepository.saveAll(newPosts);
        List<PostEntity> posts = postRepository.findAll();

        System.out.println("======== saveAll ========");
        System.out.println(posts);
    }

}