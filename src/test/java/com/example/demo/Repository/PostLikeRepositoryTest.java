package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
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

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PostLikeRepositoryTest {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private List<PostLikeEntity> postLikes = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    private UserEntity uid;

    @Autowired
    public PostLikeRepositoryTest(PostLikeRepository postLikeRepository,
            PostRepository postRepository,UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.uid = UserEntity.builder().username("user1").email("email1@gmail.com").build();
        setObjects();
    }

    void setObjects() {
        PostEntity pid1 = PostEntity.builder().title("title1").contents("contents1").build();
        PostEntity pid2 = PostEntity.builder().title("title2").contents("contents2").build();
        posts.add(pid1);
        posts.add(pid2);
        postLikes.add(PostLikeEntity.builder().uid(uid).pid(pid1).good(true).hate(false).build());
        postLikes.add(PostLikeEntity.builder().uid(uid).pid(pid2).good(false).hate(true).build());
    }

    @BeforeEach
    void setPostLikes(){
        userRepository.save(uid);
        postRepository.saveAll(posts);
        postLikeRepository.saveAll(postLikes);
    }

    @Test
    public void findAll() {
        List<PostLikeEntity> postLikes = postLikeRepository.findAll();

        System.out.println("======== findAll ========");
        System.out.println(postLikes);
    }

    @Test
    public void findByUid() {
        UserEntity uid = UserEntity.builder().uid(1L).build();
        List<PostLikeEntity> postLikes = postLikeRepository.findByUid(uid);

        System.out.println("======== findByUid ========");
        System.out.println(postLikes);
    }

    @Test
    public void saveAll() {
        List<PostEntity> newPosts = new ArrayList<>();
        List<PostLikeEntity> newPostLikes = new ArrayList<>();

        UserEntity uid = UserEntity.builder().username("user2").email("email2@gmail.com").build();
        PostEntity pid1 = PostEntity.builder().title("title1").contents("contents1").build();
        PostEntity pid2 = PostEntity.builder().title("title2").contents("contents2").build();
        newPosts.add(pid1);
        newPosts.add(pid2);
        newPostLikes.add(PostLikeEntity.builder().uid(uid).pid(pid1).good(true).hate(false).build());
        newPostLikes.add(PostLikeEntity.builder().uid(uid).pid(pid2).good(false).hate(true).build());

        userRepository.save(uid);
        postRepository.saveAll(newPosts);
        postLikeRepository.saveAll(newPostLikes);
        List<PostLikeEntity> result = postLikeRepository.findAll();

        System.out.println("======== saveAll ========");
        System.out.println(result);
    }

    @Test
    public void countByPid() {
        PostEntity pid = PostEntity.builder().pid(1L).build();
        Long countLikes = postLikeRepository.countByPid(pid);

        System.out.println("======== countByPid ========");
        System.out.println(countLikes);
    }
}
