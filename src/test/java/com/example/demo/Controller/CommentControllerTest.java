package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private final MockMvc mvc;

    @MockBean
    private final CommentService commentService;

    CommentControllerTest(MockMvc Mockmvc, CommentService commentService) {
        this.mvc = Mockmvc;
        this.commentService = commentService;
    }

    @Test
    void getCommentsByPost() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder().contents(new ArrayList<>())
                .totalPages(2).size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC,"cid")).build();
        when(commentService.getPostCommentPage(any(Long.class),any(Pageable.class))).thenReturn(commentPageDto);
        mvc.perform(get("/post/1/comments").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getCommentsByUser() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder().contents(new ArrayList<>())
                .totalPages(2).size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC,"cid")).build();
        mvc.perform(get("/user/1/comments").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void saveComment() throws Exception {
    }

    @Test
    void getCommentLikeByUser() throws Exception {
    }

    @Test
    void getCommentLikeByComment() throws Exception {
    }
}