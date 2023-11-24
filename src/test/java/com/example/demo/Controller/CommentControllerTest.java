package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private final CommentService commentService;

    @Autowired
    CommentControllerTest(RestDocumentationResultHandler restDocs, MockMvc Mockmvc,
            ObjectMapper objectMapper,
            CommentService commentService) {
        super(restDocs, Mockmvc);
        this.objectMapper = objectMapper;
        this.commentService = commentService;
    }

    @Test
    void getCommentsByPost() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder().contents(new ArrayList<>())
                .totalPages(2).size(3).numberOfElements(3).sorted(true)
                .build();
        when(commentService.getCommentByPost(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(
                        get("/comment/post/{pid}?page=0&size=10&sort=createdAt", 1).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").optional().description("게시글 PK")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 게시글 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("내용"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("size").description("페이지 게시글 수"),
                                fieldWithPath("numberOfElements").description("현재 페이지 게시글 수"),
                                fieldWithPath("sorted").description("정렬 상태")
                        )
                ))
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
                .totalPages(2).size(3).numberOfElements(3).sorted(true)
                .build();
        when(commentService.getCommentByUser(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(
                        get("/comment/user/{uid}?page=0&size=10&sort=createdAt", 1).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("uid").optional().description("유저 PK")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 게시글 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("내용"),
                                fieldWithPath("totalPages").description("총 페이지 수"),
                                fieldWithPath("size").description("페이지 게시글 수"),
                                fieldWithPath("numberOfElements").description("현재 페이지 게시글 수"),
                                fieldWithPath("sorted").description("정렬 상태")
                        )
                ))
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
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("cid").ignored(),
                                fieldWithPath("pid").optional().description("게시글 FK"),
                                fieldWithPath("uid").optional().description("유저 FK"),
                                fieldWithPath("username").optional().description("현재 유저명"),
                                fieldWithPath("contents").optional().description("내용"),
                                fieldWithPath("updatedAt").ignored()
                        ),
                        responseFields(
                                fieldWithPath("cid").description("댓글 PK"),
                                fieldWithPath("pid").description("게시글 FK"),
                                fieldWithPath("uid").description("유저 FK"),
                                fieldWithPath("username").description("현재 유저명"),
                                fieldWithPath("contents").description("내용"),
                                fieldWithPath("updatedAt").description("수정 시간")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").exists());
    }

    @Test
    void getCommentLikeByUser() throws Exception {
        List<CommentLikeDto> result = Arrays.asList(
                CommentLikeDto.builder().cid(1L).uid(1L).likes(true).build(),
                CommentLikeDto.builder().cid(2L).uid(1L).likes(true).build());
        when(commentService.getCommentLikeUid(any(Long.class), any(boolean.class))).thenReturn(
                result);
        mvc.perform(get("/comment/user/{uid}/likes/{likes}", 1, true).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("uid").optional().description("유저 FK"),
                                parameterWithName("likes").optional().description("좋아요 or 싫어요")
                        ),
                        responseFields(
                                fieldWithPath("[].cid").description("댓글 PK"),
                                fieldWithPath("[].uid").description("유저 FK"),
                                fieldWithPath("[].likes").description("좋아요 상태")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentLikeByComment() throws Exception {
        List<CommentLikeDto> result = Arrays.asList(
                CommentLikeDto.builder().cid(1L).uid(1L).likes(true).build(),
                CommentLikeDto.builder().cid(1L).uid(2L).likes(true).build());
        when(commentService.getCommentLikeCid(any(Long.class), any(boolean.class))).thenReturn(
                result);
        mvc.perform(get("/comment/{cid}/likes/{likes}", 1, true).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("cid").optional().description("댓글 FK"),
                                parameterWithName("likes").optional().description("좋아요 or 싫어요")
                        ),
                        responseFields(
                                fieldWithPath("[].cid").description("댓글 PK"),
                                fieldWithPath("[].uid").description("유저 FK"),
                                fieldWithPath("[].likes").description("좋아요 상태")
                        )
                ))
                .andExpect(status().isOk());
    }
}