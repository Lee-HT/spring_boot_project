package com.example.demo.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.CommentConverter;
import com.example.demo.Converter.CommentLikeConverter;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.CommentLikeRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Impl.CommentServiceImpl;
import org.assertj.core.api.Assertions;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private CommentConverter commentConverter;
    @Mock
    private CommentLikeConverter commentLikeConverter;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CommentServiceImpl commentService;
    private Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "cid");
    private List<CommentEntity> comments = new ArrayList<>();
    private List<CommentDto> commentDtos = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    private int MaxIdx;

    @Autowired
    public CommentServiceTest() {
        for (int i = 1; i < 5; i++) {
            users.add(UserEntity.builder().uid((long) i).username("user" + i).email("email" + i)
                    .build());
            posts.add(
                    PostEntity.builder().pid((long) i).uid(users.get(i - 1)).title("title" + i)
                            .contents("contents" + i).category("category1").build());
        }
        for (int i = 1; i < 7; i++) {
            comments.add(CommentEntity.builder().cid((long) i).pid(posts.get((i - 1) / 2))
                    .uid(users.get(i / 2)).contents("content" + i).build());
            commentDtos.add(
                    CommentDto.builder().cid((long) i).pid((long) i).uid((long) i)
                            .username("user" + i)
                            .contents("content" + i)
                            .build());
        }
        MaxIdx = comments.size();
    }

    // SecurityContext 지정
    private void setUserContextByUsername() {
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken("user", null, null));
    }

    @Test
    public void getPostCommentPage() {
        System.out.println("======== getPostCommentPage ========");
        when(postRepository.findByPid(any(Long.class))).thenReturn(posts.get(0));
        when(commentRepository.findByPid(any(PostEntity.class), any(Pageable.class))).thenReturn(
                new PageImpl<>(new ArrayList<>()));
        when(commentConverter.toDto(any(Page.class))).thenReturn(
                CommentPageDto.builder().build());
        CommentPageDto result = commentService.getCommentByPost(1L, pageable);

        System.out.println(result);

        Assertions.assertThat(result).isInstanceOf(CommentPageDto.class);
    }

    @Test
    public void getUserCommentPage() {
        System.out.println("======== getUserCommentPage ========");
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(0));
        when(commentRepository.findByUid(any(UserEntity.class), any(Pageable.class))).thenReturn(
                new PageImpl<>(new ArrayList<>()));
        when(commentConverter.toDto(any(Page.class))).thenReturn(
                CommentPageDto.builder().build());
        CommentPageDto result = commentService.getCommentByUser(1L, pageable);

        System.out.println(result);

        Assertions.assertThat(result).isInstanceOf(CommentPageDto.class);
    }

    @Test
    public void saveComment() {
        System.out.println("======== saveComment ========");
        setUserContextByUsername();
        when(userRepository.findByProvider(any(String.class))).thenReturn(users.get(0));
        when(postRepository.findByPid(any(Long.class))).thenReturn(posts.get(0));
        when(commentConverter.toEntity(any(CommentDto.class), any(UserEntity.class),
                any(PostEntity.class))).thenReturn(comments.get(0));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comments.get(0));
        when(commentConverter.toDto(any(CommentEntity.class))).thenReturn(commentDtos.get(0));
        CommentDto result = commentService.saveComment(commentDtos.get(0));

        CommentDto commentDto = CommentDto.builder().cid(1L).pid(1L).uid(1L).username("user1")
                .contents("content1").build();
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(commentDto);
    }

    @Test
    public void getCommentLikeCid() {
        System.out.println("======== getCommentLikeCid ========");
        when(commentRepository.findByCid(any(Long.class))).thenReturn(comments.get(0));
        when(commentLikeRepository.findByCidAndLikes(any(CommentEntity.class),
                any(Boolean.class))).thenReturn(new ArrayList<>());
        when(commentLikeConverter.toDto(any(List.class))).thenReturn(new ArrayList<>());
        List<CommentLikeDto> result = commentService.getCommentLikeCid(1L, true);

        System.out.println(result);

        Assertions.assertThat(result).isInstanceOf(List.class);
    }

    @Test
    public void getCommentLikeUid() {
        System.out.println("======== getCommentLikeUid ========");
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(0));
        when(commentLikeRepository.findByUidAndLikes(any(UserEntity.class),
                any(Boolean.class))).thenReturn(new ArrayList<>());
        when(commentLikeConverter.toDto(any(List.class))).thenReturn(new ArrayList<>());
        List<CommentLikeDto> result = commentService.getCommentLikeUid(1L, true);

        System.out.println(result);

        Assertions.assertThat(result).isInstanceOf(List.class);
    }

}
