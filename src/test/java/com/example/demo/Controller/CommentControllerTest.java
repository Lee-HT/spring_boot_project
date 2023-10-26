package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final CommentService commentService;

    @Autowired
    CommentControllerTest(MockMvc Mockmvc, ObjectMapper objectMapper,
            CommentService commentService) {
        this.mvc = Mockmvc;
        this.objectMapper = objectMapper;
        this.commentService = commentService;
    }

    @Test
    void getCommentsByPost() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder().contents(new ArrayList<>())
                .totalPages(2).size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "cid"))
                .build();
        when(commentService.getPostCommentPage(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(get("/post/1/comments").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.sorted").exists());
    }

    @Test
    void getCommentsByUser() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder().contents(new ArrayList<>())
                .totalPages(2).size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "cid"))
                .build();
        when(commentService.getUserCommentPage(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(get("/user/1/comments").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.sorted").exists());
    }

    @Test
    void saveComment() throws Exception {
        CommentDto commentDto = CommentDto.builder().pid(1L).uid(1L).username("user")
                .contents("contents").build();
        when(commentService.saveComment(any(CommentDto.class))).thenReturn(commentDto);
        mvc.perform(post("/comment").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)).with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").exists());
    }

    @Test
    void getCommentLikeByUser() throws Exception {
        when(commentService.getCommentLikeUid(any(Long.class),any(boolean.class))).thenReturn(new ArrayList<>());
        mvc.perform(get("/user/1/comment-likes").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getCommentLikeByComment() throws Exception {
        when(commentService.getCommentLikeCid(any(Long.class),any(boolean.class))).thenReturn(new ArrayList<>());
        mvc.perform(get("/comment/1/comment-likes").with(oauth2Login())).andDo(print())
                .andExpect(status().isOk());
    }
}