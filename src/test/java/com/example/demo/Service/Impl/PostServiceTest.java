package com.example.demo.Service.Impl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.PostMapper;
import com.example.demo.Repository.PostLikeRepository;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private PostLikeRepository postLikeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private PostConverter postConverter;
    @InjectMocks
    private PostServiceImpl postService;
    private List<PostEntity> posts = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();
    private List<PostDto> postDtos = new ArrayList<>();
    private List<PostLikeEntity> postLikes = new ArrayList<>();
    private Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "pid");
    private int maxIdx;

    @Autowired
    public PostServiceTest() {
        for (int i = 1; i < 3; i++) {
            users.add(UserEntity.builder().uid((long) i).username("user" + i)
                    .email("email" + i + "@gmail.com")
                    .build());
        }
        for (int i = 1; i < 6; i++) {
            posts.add(PostEntity.builder().pid((long) i).username(users.get(0))
                    .title("title" + i)
                    .contents("contents" + i)
                    .build());
        }
        for (int i = 1; i < 6; i++) {
            postDtos.add(PostDto.builder().pid((long) i).title("title" + i)
                    .contents("contents" + i).build());
        }
        for (int i = 0; i < 5; i++) {
            postLikes.add(
                    PostLikeEntity.builder().pid(posts.get(i)).uid(users.get(i / 3))
                            .likes(i % 2 == 0)
                            .hate(i % 2 != 0).build());
        }
        this.maxIdx = posts.size();
    }

    @Test
    @DisplayName("TITLE 기준 SELECT")
    public void findPostByTitle() {
        Page<PostEntity> pages = new PageImpl<>(
                new ArrayList<>(this.posts.subList(maxIdx - 3, maxIdx)), this.pageable, maxIdx);
        PostPageDto pageDto = PostPageDto.builder()
                .contents(new ArrayList<>(this.postDtos.subList(maxIdx - 3, maxIdx)))
                .totalPages(pages.getTotalPages())
                .numberOfElements(pages.getNumberOfElements()).size(pages.getSize())
                .sorted(pages.getSort()).build();
        when(postRepository.findByTitleContaining("title", this.pageable)).thenReturn(pages);
        when(postConverter.toDto(pages)).thenReturn(pageDto);
        PostPageDto result = postService.findPostByTitle("title", pageable);

        Assertions.assertThat(result).isEqualTo(pageDto);

        System.out.println("======== findByTitleContaining ========");
        System.out.println(result);
    }

    @Test
    @DisplayName("USERNAME 기준 SELECT")
    public void findPostByUsername() {
        String username = "user1";
        Page<PostEntity> pages = new PageImpl<>(
                new ArrayList<>(this.posts.subList(maxIdx - 3, maxIdx)), this.pageable, maxIdx);
        PostPageDto pageDto = PostPageDto.builder()
                .contents(new ArrayList<>(this.postDtos.subList(maxIdx - 3, maxIdx)))
                .totalPages(pages.getTotalPages())
                .numberOfElements(pages.getNumberOfElements()).size(pages.getSize())
                .sorted(pages.getSort()).build();
        when(userRepository.findByUsername(any(String.class))).thenReturn(users.get(0));
        when(postRepository.findByUsername(users.get(0), this.pageable)).thenReturn(pages);
        when(postConverter.toDto(pages)).thenReturn(pageDto);
        PostPageDto result = postService.findPostByUsername(username, this.pageable);

        Assertions.assertThat(result).isEqualTo(pageDto);

        System.out.println("======== findPostByUsername ========");
        System.out.println(posts);
    }

    @Test
    @DisplayName("INSERT")
    public void savePost() {
        PostDto postDto = PostDto.builder().title("title2").contents("contents2").build();
        when(postConverter.toEntity(any(PostDto.class))).thenReturn(this.posts.get(1));
        when(postRepository.save(any(PostEntity.class))).thenReturn(this.posts.get(1));
        when(postConverter.toDto(any(PostEntity.class))).thenReturn(postDto);
        PostDto post = postService.savePost(postDto);

        Assertions.assertThat(post).isEqualTo(postDto);

        System.out.println("======== savePost ========");
        System.out.println(post);
    }

    @Test
    @DisplayName("UPDATE")
    public void updatePost() {
        PostDto postDto = PostDto.builder().pid(1L).title("title3").contents("contents3").build();
        PostEntity postEntity = PostEntity.builder().pid(1L).title("title1").contents("contents1")
                .build();
        when(postRepository.findByPid(1L)).thenReturn(postEntity);
        when(postConverter.toDto(any(PostEntity.class))).thenReturn(postDto);
        PostDto result = postService.updatePost(postDto);

        Assertions.assertThat(result).isEqualTo(postDto);

        System.out.println("======== updatePost ========");
        System.out.println(result);
    }

    @Test
    @DisplayName("DELETE")
    public void deletePost() {
        List<Long> pid = Arrays.asList(1L, 2L);
        when(postRepository.findByPid(1L)).thenReturn(posts.get(0));
        when(postRepository.findByPid(2L)).thenReturn(posts.get(1));
        int count = postService.deletePosts(pid);

        Assertions.assertThat(count).isEqualTo(2);

        System.out.println("======== deletePost ========");
        System.out.println(count);
    }

    @Test
    @DisplayName("POST 좋아요")
    public void likesPost() {
        Boolean check = this.postLikes.get(0).isLikes();
        when(postRepository.findByPid(any(Long.class))).thenReturn(posts.get(0));
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(0));
        when(postLikeRepository.findByPidAndUid(posts.get(0), users.get(0))).thenReturn(
                this.postLikes.get(0));
        boolean result = postService.likePost(posts.get(0).getPid(), users.get(0).getUid());

        Assertions.assertThat(result).isEqualTo(!check);

        System.out.println("======== likesPost ========");
        System.out.println(result);
    }

    @Test
    @DisplayName("POST 싫어요")
    public void hatePost() {
        Boolean check = this.postLikes.get(0).isHate();
        when(postRepository.findByPid(any(Long.class))).thenReturn(posts.get(1));
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(1));
        when(postLikeRepository.findByPidAndUid(posts.get(1), users.get(1))).thenReturn(
                this.postLikes.get(1));
        boolean result = postService.hatePost(posts.get(1).getPid(), users.get(1).getUid());

        Assertions.assertThat(result).isEqualTo(check);

        System.out.println("======== hatePost ========");
        System.out.println(result);
    }
}