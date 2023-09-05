package com.example.demo.Service.Impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.CommentConverter;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentConverter commentConverter;
    @InjectMocks
    private CommentServiceImpl commentService;
    private Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "cid");
    private List<CommentEntity> comments = new ArrayList<>();
    private List<CommentDto> commentDtos = new ArrayList<>();
    private List<UserEntity> users = new ArrayList<>();
    private List<PostEntity> posts = new ArrayList<>();
    private int MaxIdx = comments.size();

    @Autowired
    public CommentServiceTest() {
        for (int i = 0; i < 4; i++) {
            users.add(UserEntity.builder().uid((long) i + 1).username("user" + i).email("email" + i)
                    .build());
            posts.add(
                    PostEntity.builder().pid((long) i + 1).username(users.get(i)).title("title" + i)
                            .contents("contents" + i).category("category1").build());
        }
        for (int i = 0; i < 6; i++) {
            comments.add(CommentEntity.builder().cid((long) i + 1).pid(posts.get(i / 2))
                    .username(users.get((i + 1) / 2)).contents("content" + i).build());
            commentDtos.add(
                    CommentDto.builder().cid((long) i).username("user" + i).contents("content" + i)
                            .build());
        }
    }

    @Test
    public void getComment() {
        System.out.println("======== getComment ========");
        Long pid = 1L;
        Page<CommentEntity> pages = new PageImpl<>(new ArrayList<>(comments.subList(0, 2)),
                pageable,
                MaxIdx);
        CommentPageDto commentPageDto = CommentPageDto.builder()
                .contents(new ArrayList<>(commentDtos.subList(0, 2)))
                .totalPages(pages.getTotalPages()).size(pages.getSize())
                .numberOfElements(pages.getNumberOfElements()).sorted(pages.getSort()).build();
        when(postRepository.findByPid(any(Long.class))).thenReturn(posts.get(0));
        when(commentRepository.findByPid(any(PostEntity.class), any(Pageable.class))).thenReturn(
                pages);
        when(commentConverter.toDto(pages)).thenReturn(commentPageDto);
        CommentPageDto result = commentService.getCommentPage(pid, pageable);

        Assertions.assertThat(result).isEqualTo(commentPageDto);
    }

    @Test
    public void saveComment() {
        System.out.println("======== saveComment ========");
        when(commentConverter.toEntity(commentDtos.get(0),users.get(0))).thenReturn(comments.get(0));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(comments.get(0));
        when(commentConverter.toDto(any(CommentEntity.class))).thenReturn(commentDtos.get(0));
        CommentDto result = commentService.saveComment(commentDtos.get(0),users.get(0));

        Assertions.assertThat(result).isEqualTo(commentDtos.get(0));
    }

}
