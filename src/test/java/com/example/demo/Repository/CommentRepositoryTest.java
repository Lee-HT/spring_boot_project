package com.example.demo.Repository;

import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final List<CommentEntity> comments = new ArrayList<>();
    private final List<UserEntity> users = new ArrayList<>();
    private final List<PostEntity> posts = new ArrayList<>();
    private final Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "cid");
    private int maxIdx;
    private final List<PostEntity> pk = new ArrayList<>();


    @Autowired
    public CommentRepositoryTest(CommentRepository commentRepository,
            UserRepository userRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setComments() {
        for (int i = 1; i < 4; i++) {
            users.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com").provider("google_" + i).build());
            posts.add(PostEntity.builder().uid(users.get(i - 1)).title("title" + i)
                    .contents("contents" + i).category("category1").build());
        }
        for (int i = 0; i < 4; i++) {
            comments.add(CommentEntity.builder().uid(users.get(i / 2)).pid(posts.get(0))
                    .contents("contents" + (i + 1)).build());
        }
        userRepository.saveAll(users);
        postRepository.saveAll(posts);
        commentRepository.saveAll(comments);
        maxIdx = comments.size();
        pk.addAll(postRepository.findAll());
    }

    @Test
    void findAll() {
        List<CommentEntity> comments = commentRepository.findAll();

        Assertions.assertThat(comments).usingRecursiveComparison().isEqualTo(this.comments);
    }

    @Test
    void findByPid() {
        Page<CommentEntity> pages = new PageImpl<>(
                new ArrayList<>(comments.subList(maxIdx - 3, maxIdx)), pageable, maxIdx);
        Page<CommentEntity> result = commentRepository.findByPid(pk.get(0), pageable);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);
    }

    @Test
    void findByUid() {
        Page<CommentEntity> pages = new PageImpl<>(new ArrayList<>(comments.subList(0, 2)),
                pageable, maxIdx);
        Page<CommentEntity> comments = commentRepository.findByUid(users.get(0), pageable);

        Assertions.assertThat(comments).usingRecursiveComparison().isEqualTo(pages);
    }

    @Test
    void saveAll() {
        List<CommentEntity> comments = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            comments.add(CommentEntity.builder().uid(users.get(2)).contents("contents" + i)
                    .build());
        }
        List<CommentEntity> result = commentRepository.saveAll(comments);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(comments);
    }

}