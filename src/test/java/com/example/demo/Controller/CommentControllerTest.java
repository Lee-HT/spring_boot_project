package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.CommentDto;
import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.DTO.CommentPageDto;
import com.example.demo.Service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private CommentService commentService;

    @Autowired
    CommentControllerTest(RestDocumentationResultHandler restDocs, MockMvc Mockmvc,
            ObjectMapper objectMapper) {
        super(restDocs, Mockmvc);
        this.objectMapper = objectMapper;
    }

    @Test
    void getCommentsByPost() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder()
                .contents(Collections.singletonList(CommentDto.builder().build())).totalPages(2)
                .size(3).numberOfElements(3).totalElements(6L).sorted(true).build();
        when(commentService.getCommentByPost(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(get("/comment/post/{pid}?page=0&size=10&sort=createdAt", 1).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("pid").description("게시글 PK")),
                        getPageQuerySnippet(),
                        getCommentPageSnippet()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getCommentsByUser() throws Exception {
        CommentPageDto commentPageDto = CommentPageDto.builder()
                .contents(Collections.singletonList(CommentDto.builder().build()))
                .totalPages(2).size(3).numberOfElements(3).totalElements(6L).sorted(true).build();
        when(commentService.getCommentByUser(any(Long.class), any(Pageable.class))).thenReturn(
                commentPageDto);
        mvc.perform(get("/comment/user/{uid}?page=0&size=10&sort=createdAt", 1).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("uid").description("유저 PK")),
                        getPageQuerySnippet(),
                        getCommentPageSnippet()
                ))
                .andExpect(status().isOk());
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
                                parameterWithName("uid").description("유저 FK"),
                                parameterWithName("likes").description("좋아요 or 싫어요")),
                        getCommentLikeResponseSnippet()
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
                                parameterWithName("cid").description("댓글 FK"),
                                parameterWithName("likes").description("좋아요 or 싫어요")),
                        getCommentLikeResponseSnippet()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void saveComment() throws Exception {
        CommentDto commentDto = CommentDto.builder().pid(1L).uid(1L).cid(1L).username("user")
                .contents("contents").build();
        when(commentService.saveComment(any(CommentDto.class))).thenReturn(1L);
        mvc.perform(post("/comment").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)).with(csrf()))
                .andDo(restDocs.document(
                        getCommentRequestSnippet()
                ))
                .andExpect(status().isCreated());
    }

    @Test
    void updateComment() throws Exception {
        CommentDto commentDto = CommentDto.builder().cid(1L).pid(1L).uid(1L).username("username")
                .contents("contents").build();
        when(commentService.updateComment(any(CommentDto.class))).thenReturn(1L);
        mvc.perform(patch("/comment").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andDo(restDocs.document(
                        getCommentRequestSnippet()
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    void putCommentLike() throws Exception {
        CommentLikeDto commentLikeDto = CommentLikeDto.builder().uid(1L).cid(1L).likes(true).build();
        when(commentService.saveCommentLike(any(CommentLikeDto.class))).thenReturn(201);
        mvc.perform(put("/comment/likes").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentLikeDto)))
                .andDo(restDocs.document(
                        getCommentLikeRequestSnippet()
                ))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteComment() throws Exception {
        when(commentService.deleteComment(anyLong())).thenReturn(1L);
        mvc.perform(delete("/comment/{cid}", 1L).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("cid").description("댓글 PK"))
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCommentLike() throws Exception {
        when(commentService.deleteCommentLike(anyLong())).thenReturn(1L);
        mvc.perform(delete("/comment/{cid}/likes", 1L).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("cid").description("댓글 PK"))
                ))
                .andExpect(status().isNoContent());
    }

    private Snippet getPageQuerySnippet() {
        return queryParameters(
                parameterWithName("page").optional().description("페이지 번호"),
                parameterWithName("size").optional().description("페이지당 게시글 수"),
                parameterWithName("sort").optional().description("정렬 기준"));
    }

    private Snippet getCommentPageSnippet() {
        return responseFields(
                fieldWithPath("contents").description("댓글 리스트"),
                fieldWithPath("totalPages").description("총 페이지 수"),
                fieldWithPath("size").description("페이지 게시글 수"),
                fieldWithPath("numberOfElements").description("현재 페이지 게시글 수"),
                fieldWithPath("totalElements").description("전체 게시글 수"),
                fieldWithPath("sorted").description("정렬 상태"),

                fieldWithPath("contents.[].cid").description("댓글 PK"),
                fieldWithPath("contents.[].pid").description("게시글 FK"),
                fieldWithPath("contents.[].uid").description("유저 FK"),
                fieldWithPath("contents.[].username").description("유저명"),
                fieldWithPath("contents.[].contents").description("내용"),
                fieldWithPath("contents.[].updatedAt").ignored());
    }

    private Snippet getCommentRequestSnippet() {
        return requestFields(
                fieldWithPath("cid").description("댓글 PK"),
                fieldWithPath("pid").description("게시글 FK"),
                fieldWithPath("uid").description("유저 FK"),
                fieldWithPath("username").description("유저명"),
                fieldWithPath("contents").description("내용"),
                fieldWithPath("updatedAt").ignored());
    }

    private Snippet getCommentLikeResponseSnippet() {
        return responseFields(
                fieldWithPath("[].cid").description("댓글 PK"),
                fieldWithPath("[].uid").description("유저 FK"),
                fieldWithPath("[].likes").description("좋아요 상태"));
    }

    private Snippet getCommentLikeRequestSnippet() {
        return requestFields(
                fieldWithPath("cid").description("댓글 PK"),
                fieldWithPath("uid").description("유저 FK"),
                fieldWithPath("likes").description("좋아요 상태"));
    }
}