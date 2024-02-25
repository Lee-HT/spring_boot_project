package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
class PostRepositoryTest {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final List<UserEntity> users = new ArrayList<>();
    private final List<PostEntity> posts = new ArrayList<>();
    // 0 페이지, 페이지당 3개, 내림차순, 정렬기준 pid
    private final Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "pid");
    private final List<Long> pk = new ArrayList<>();

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setPosts() {
        for (int i = 1; i < 3; i++) {
            users.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com").provider("google_" + i).build());
        }
        for (int i = 1; i < 6; i++) {
            posts.add(PostEntity.builder().title("title" + i).contents("content" + i)
                    .uid(users.get(0)).username("user" + 1).category("category" + i).build());
        }

        userRepository.saveAll(users);
        postRepository.saveAll(posts);

        for (PostEntity ett : postRepository.findAll()) {
            pk.add(ett.getPid());
        }
    }

    @Test
    void findAll() {
        List<PostEntity> posts = postRepository.findAll();

        Assertions.assertThat(posts).usingRecursiveComparison().isEqualTo(this.posts);
    }

    @Test
    void findByPid() {
        Optional<PostEntity> result = postRepository.findByPid(pk.get(0));

        Assert.isTrue(result.isPresent());
        Assertions.assertThat(result.get()).usingRecursiveComparison().isEqualTo(posts.get(0));
    }

    @Test
    void findAllPaging() {
        Page<PostEntity> result = postRepository.findAll(this.pageable);
        Page<PostEntity> pages = new PageImpl<>(List.of(posts.get(4),posts.get(3),posts.get(2)), this.pageable,
                this.posts.size());

        Assertions.assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(pages.getContent());
        Assertions.assertThat(result.getTotalElements()).usingRecursiveComparison().isEqualTo(pages.getTotalElements());
    }

    @Test
    void findByCategoryPaging() {
        Page<PostEntity> result = postRepository.findByCategory("category1",this.pageable);
        Page<PostEntity> pages = new PageImpl<>(List.of(this.posts.get(0)),this.pageable,1);
        Assertions.assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(pages.getContent());
        Assertions.assertThat(result.getTotalElements()).usingRecursiveComparison().isEqualTo(pages.getTotalElements());
    }

    @Test
    void findByUsernamePaging() {
        Page<PostEntity> result = postRepository.findByUsernameContaining("user", this.pageable);
        Page<PostEntity> pages = new PageImpl<>(List.of(posts.get(4),posts.get(3),posts.get(2)), this.pageable, this.posts.size());

        Assertions.assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(pages.getContent());
        Assertions.assertThat(result.getTotalElements()).usingRecursiveComparison().isEqualTo(pages.getTotalElements());
    }

    @Test
    void findByTitlePaging() {
        Page<PostEntity> result = postRepository.findByTitleContaining("title1", this.pageable);
        Page<PostEntity> pages = new PageImpl<>(List.of(this.posts.get(0)), this.pageable,
                1);

        Assertions.assertThat(result.getContent()).usingRecursiveComparison().isEqualTo(pages.getContent());
        Assertions.assertThat(result.getTotalElements()).usingRecursiveComparison().isEqualTo(pages.getTotalElements());
    }


    @Test
    void saveAll() {
        List<PostEntity> Posts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Posts.add(PostEntity.builder().title("title3").contents("contents3")
                    .uid(this.users.get(1)).category("category3").build());
        }
        List<PostEntity> result = postRepository.saveAll(Posts);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(Posts);
    }

}