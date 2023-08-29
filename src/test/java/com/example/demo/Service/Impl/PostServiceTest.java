package com.example.demo.Service.Impl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.PostConverter;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.PostMapper;
import com.example.demo.Repository.PostRepository;
import com.example.demo.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
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

    @Autowired
    public PostServiceTest(){
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        posts.add(PostEntity.builder().title("title1").contents("contents1").build());
        posts.add(PostEntity.builder().title("title2").contents("contents2").build());
    }

    @Test
    public void findPostByTitle(){
        when(postRepository.findByTitleContaining("title")).thenReturn(this.posts);
        List<PostEntity> posts = postService.findPostByTitle("title");

        Assertions.assertThat(posts).isEqualTo(this.posts);

        System.out.println("======== findByTitleContaining ========");
        System.out.println(posts);
    }

    @Test
    public void findPostByUsername(){
        String username = "user1";
        when(userRepository.findByUsername(any(String.class))).thenReturn(users.get(0));
        when(postRepository.findByUsername(users.get(0))).thenReturn(posts);
        List<PostEntity> posts = postService.findPostByUsername(username);

        Assertions.assertThat(posts).isEqualTo(this.posts);

        System.out.println("======== findPostByUsername ========");
        System.out.println(posts);
    }

    @Test
    public void savePost(){
        PostDto postDto = PostDto.builder().pid(1L).title("title2").contents("contents2").build();
        when(postRepository.save(any(PostEntity.class))).thenReturn(this.posts.get(1));
        when(postConverter.toEntity(any(PostDto.class))).thenReturn(this.posts.get(1));
        PostEntity post = postService.savePost(postDto);

        Assertions.assertThat(post).isEqualTo(this.posts.get(1));

        System.out.println("======== savePost ========");
        System.out.println(post);
    }

    @Test
    public void updatePost(){
        PostDto postDto = PostDto.builder().pid(1L).title("title3").contents("contents3").build();
        PostEntity newPost = PostEntity.builder().pid(1L).title("title3").contents("contents3").build();
        when(postRepository.save(any(PostEntity.class))).thenReturn(newPost);
        when(postRepository.findByPid(1L)).thenReturn(posts.get(0));
        PostEntity post = postService.updatePost(postDto);

        Assertions.assertThat(post).isEqualTo(newPost);

        System.out.println("======== updatePost ========");
        System.out.println(post);
    }

    @Test
    public void deletePost(){
        List<Long> pid = Arrays.asList(1L,2L);
        when(postRepository.findByPid(1L)).thenReturn(posts.get(0));
        when(postRepository.findByPid(2L)).thenReturn(posts.get(1));
        int count = postService.deletePosts(pid);

        Assertions.assertThat(count).isEqualTo(2);

        System.out.println("======== deletePost ========");
        System.out.println(count);
    }
}