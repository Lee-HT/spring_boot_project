package com.example.demo.Repository;

import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    private List<UserEntity> users = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    // 0 페이지, 페이지당 3개, 내림차순, 정렬기준 pid
    private Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "pid");
    private int maxIdx;

    @Autowired
    public PostRepositoryTest(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setPosts() {
        for (int i = 1; i < 3; i++) {
            users.add(UserEntity.builder().username("user" + i)
                    .email("email" + i + "@gmail.com").build());
        }
        for (int i = 1; i < 6; i++) {
            posts.add(PostEntity.builder().title("title" + i).contents("content" + i)
                    .username(users.get(0)).category("category" + i).build());
        }
        this.maxIdx = this.posts.size();

        userRepository.saveAll(users);
        postRepository.saveAll(posts);
    }

    @Test
    @DisplayName("전체 SELECT")
    public void findAll() {
        System.out.println("======== findAll ========");
        List<PostEntity> posts = postRepository.findAll();

        Assertions.assertThat(posts).usingRecursiveComparison().isEqualTo(this.posts);

        System.out.println(posts);
    }

    @Test
    @DisplayName("USERNAME 기준 SELECT")
    public void findByUsernamePaging() {
        System.out.println("======== findByUsernamePaging ========");
        Page<PostEntity> result = postRepository.findByUsername(users.get(0), this.pageable);
        Page<PostEntity> pages = new PageImpl<>(
                new ArrayList<>(this.posts.subList(maxIdx - 3, maxIdx)),
                this.pageable,
                this.posts.size());

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);

        System.out.println(result.getContent());
    }

    @Test
    @DisplayName("TITLE 기준 SELECT")
    public void findByTitlePaging() {
        System.out.println("======== findByTitlePaging ========");
        Page<PostEntity> result = postRepository.findByTitleContaining("title", this.pageable);
        Page<PostEntity> pages = new PageImpl<>(
                new ArrayList<>(this.posts.subList(maxIdx - 3, maxIdx)),
                this.pageable, this.posts.size());

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);

        System.out.println(result.getContent());
    }

    @Test
    @DisplayName("전체 SELECT PAGING")
    public void findAllPaging() {
        System.out.println("======== findAllPaging ========");
        Page<PostEntity> result = postRepository.findAll(this.pageable);
        Page<PostEntity> pages = new PageImpl<>(
                new ArrayList<>(this.posts.subList(maxIdx - 3, maxIdx)),
                this.pageable, this.posts.size());

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(pages);

        System.out.println(result.getContent());
    }

    @Test
    @DisplayName("INSERT")
    public void saveAll() {
        System.out.println("======== saveAll ========");
        List<PostEntity> Posts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Posts.add(PostEntity.builder().title("title3").contents("contents3")
                    .username(this.users.get(1)).category("category3").build());
        }
        List<PostEntity> result = postRepository.saveAll(Posts);

        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(Posts);

        System.out.println(result);
    }

}