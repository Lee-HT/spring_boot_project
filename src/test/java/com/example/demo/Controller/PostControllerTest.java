package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
//@AutoConfigureRestDocs(uriHost = "localhost", uriPort = 6550, uriScheme = "http")
class PostControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private final PostService postService;

    @Autowired
    public PostControllerTest(RestDocumentationResultHandler restDocs, MockMvc mockMvc,
            PostService postService, ObjectMapper objectMapper) {
        super(restDocs, mockMvc);
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    @Test
    void getPostPage() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(true).build();
        when(postService.findPostPage(any(Pageable.class))).thenReturn(postPageDto);

        mvc.perform(get("/post?page=0&size=10&sort=pid")
                        .with(oauth2Login()))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 게시글 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("게시글 리스트"),
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
    void savePost() throws Exception {
        PostDto post = PostDto.builder().pid(1L).uid(1L).title("title1")
                .contents("contents1")
                .username("user1").category("category1").build();
        when(postService.savePost(any(PostDto.class))).thenReturn(post);

        mvc.perform(post("/post").with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)).with(csrf())).andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("pid").ignored(),
                                fieldWithPath("uid").ignored(),
                                fieldWithPath("title").optional().description("게시글 제목"),
                                fieldWithPath("contents").optional().description("게시글 내용"),
                                fieldWithPath("username").description("현재 유저명"),
                                fieldWithPath("category").optional().description("게시글 카테고리"),
                                fieldWithPath("updatedAt").ignored(),
                                fieldWithPath("createdAt").ignored(),
                                fieldWithPath("view").ignored()
                        ),
                        responseFields(
                                fieldWithPath("pid").description("게시글 PK"),
                                fieldWithPath("uid").description("유저 FK"),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("contents").description("게시글 내용"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("category").description("카테고리"),
                                fieldWithPath("updatedAt").type("LocalDateTime")
                                        .description("수정 시간"),
                                fieldWithPath("createdAt").type("LocalDateTime")
                                        .description("생성 시간"),
                                fieldWithPath("view").description("조회수")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").exists());
    }

    @Test
    void getPost() throws Exception {
        PostDto postDto = PostDto.builder().build();
        when(postService.findPost(any(Long.class))).thenReturn(postDto);

        mvc.perform(get("/post/{pid}",1L).with(oauth2Login())).andDo(restDocs.document(
                pathParameters(
                        parameterWithName("pid").description("게시글 PK")
                ),
                responseFields(
                        fieldWithPath("pid").description("게시글 PK"),
                        fieldWithPath("uid").description("유저 PK"),
                        fieldWithPath("username").description("유저명"),
                        fieldWithPath("title").description("게시글 제목"),
                        fieldWithPath("contents").description("게시글 내용"),
                        fieldWithPath("category").description("카테고리"),
                        fieldWithPath("updatedAt").description("수정 시간"),
                        fieldWithPath("createdAt").description("생성 시간"),
                        fieldWithPath("view").description("조회 수")
                )
        ));
    }

    @Test
    void getPostByTitle() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(true).build();
        when(postService.findPostByTitle(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(get("/post/title/{title}?page=0&size=10&sort=pid", "title").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("title").optional().description("게시글 제목")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 게시글 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("게시글 리스트"),
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
    void getPostByUsername() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(true).build();
        when(postService.findPostByUsername(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(
                        get("/post/username/{username}?page=0&size=10&sort=pid", "user").with(
                                oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("username").optional().description("유저명")
                        ),
                        queryParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지당 게시글 수"),
                                parameterWithName("sort").description("정렬 기준")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("게시글 리스트"),
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
    void getPostLike() throws Exception {
        when(postService.getLike(any(Long.class), any(Long.class))).thenReturn(
                LikeDto.builder().likes(true).build());

        mvc.perform(get("/post/{pid}/username/{uid}/likes", "1", "1").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").optional().description("게시글 PK"),
                                parameterWithName("uid").optional().description("유저 FK")
                        ),
                        responseFields(
                                fieldWithPath("likes").description("좋아요 상태")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    void savePostLike() throws Exception {
        PostLikeDto dto = PostLikeDto.builder().pid(1L).uid(1L).likes(false).build();
        when(postService.likeState(any(PostLikeDto.class))).thenReturn(
                LikeDto.builder().likes(false).build());

        mvc.perform(post("/post/likes").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("pid").optional().description("게시글 PK"),
                                fieldWithPath("uid").optional().description("유저 FK"),
                                fieldWithPath("likes").optional().description("좋아요 상태")
                        ),
                        responseFields(
                                fieldWithPath("likes").description("좋아요 상태")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    void deletePostLike() throws Exception {
        when(postService.deleteLike(any(Long.class), any(Long.class))).thenReturn(1);

        mvc.perform(
                        delete("/post/{pid}/username/{uid}/likes", "1", "1").with(oauth2Login())
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").optional().description("게시글 PK"),
                                parameterWithName("uid").optional().description("유저 FK")
                        ),
                        responseBody()
                ))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }


}
