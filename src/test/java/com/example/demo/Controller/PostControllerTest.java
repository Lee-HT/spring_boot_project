package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.LikeDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostLikeDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
//@AutoConfigureRestDocs(uriHost = "localhost", uriPort = 6550, uriScheme = "http")
class PostControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @Autowired
    public PostControllerTest(RestDocumentationResultHandler restDocs, MockMvc mockMvc,
            ObjectMapper objectMapper) {
        super(restDocs, mockMvc);
        this.objectMapper = objectMapper;
    }

    @Test
    void getPostPage() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder()
                .contents(Collections.singletonList(PostDto.builder().build())).totalPages(2)
                .size(3).numberOfElements(3).totalElements(6L).sorted(true).build();
        when(postService.findPostPage(any(Pageable.class))).thenReturn(postPageDto);

        mvc.perform(get("/post?page=0&size=10&sort=pid").with(oauth2Login()))
                .andDo(restDocs.document(
                        getPageQuerySnippet(),
                        getPostPageSnippet()
                ))
                .andExpect(status().isOk());
    }


    @Test
    void getPost() throws Exception {
        PostDto postDto = PostDto.builder().pid(1L).uid(1L).username("user1").title("title1")
                .contents("contents1").category("category1").updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now()).view(0).build();
        when(postService.findPost(any(Long.class))).thenReturn(postDto);

        mvc.perform(get("/post/{pid}", 1L).with(oauth2Login())).andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").description("게시글 PK")
                        ),
                        getPostResponseSnippet()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getPostByTitle() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder()
                .contents(Collections.singletonList(PostDto.builder().build())).totalPages(2)
                .size(3).numberOfElements(3).totalElements(6L).sorted(true).build();
        when(postService.findPostByTitle(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(get("/post/title/{title}?page=0&size=10&sort=pid", "title").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("title").description("게시글 제목")
                        ),
                        getPageQuerySnippet(),
                        getPostPageSnippet()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getPostByUsername() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder()
                .contents(Collections.singletonList(PostDto.builder().build())).totalPages(2)
                .size(3).numberOfElements(3).totalElements(6L).sorted(true).build();
        when(postService.findPostByUsername(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(get("/post/username/{username}?page=0&size=10&sort=pid", "user").with(
                        oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("username").description("유저명")
                        ),
                        getPageQuerySnippet(),
                        getPostPageSnippet()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getPostLike() throws Exception {
        Map<String, Object> response = new HashMap<>() {{
            put("permit", true);
            put("contents", LikeDto.builder().likes(true).build());
        }};
        when(postService.getLike(any(Long.class))).thenReturn(
                response);

        mvc.perform(get("/post/{pid}/likes", "1").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(parameterWithName("pid").description("게시글 PK")),
                        responseFields(fieldWithPath("likes").description("좋아요 상태"))
                ))
                .andExpect(status().isOk());
    }

    @Test
    void savePost() throws Exception {
        PostDto post = PostDto.builder().pid(1L).uid(1L).title("title1").contents("contents1")
                .username("user1").category("category1").build();
        when(postService.savePost(any(PostDto.class))).thenReturn(post);

        mvc.perform(post("/post").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)).with(csrf())).andDo(print())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("pid").ignored(),
                                fieldWithPath("uid").ignored(),
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("contents").description("게시글 내용"),
                                fieldWithPath("username").description("현재 유저명"),
                                fieldWithPath("category").description("게시글 카테고리"),
                                fieldWithPath("updatedAt").ignored(),
                                fieldWithPath("createdAt").ignored(),
                                fieldWithPath("view").ignored()
                        )
                ))
                .andExpect(status().isCreated());
    }

    @Test
    void savePostLike() throws Exception {
        PostLikeDto dto = PostLikeDto.builder().pid(1L).likes(false).build();
        Map<String, Object> response = new HashMap<>() {{
            put("permit", true);
            put("contents", LikeDto.builder().likes(false).build());
        }};
        when(postService.savelikeState(any(PostLikeDto.class))).thenReturn(response);

        mvc.perform(put("/post/likes").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("pid").description("게시글 PK"),
                                fieldWithPath("uid").ignored(),
                                fieldWithPath("likes").description("좋아요 상태"))
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePost() throws Exception {
        when(postService.deletePost(any(Long.class))).thenReturn(1L);

        mvc.perform(delete("/post/{pid}", 1L).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").description("게시글 PK")
                        )
                ))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePostLike() throws Exception {
        when(postService.deletePostLike(any(Long.class))).thenReturn(1L);

        mvc.perform(delete("/post/{pid}/likes", "1").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("pid").description("게시글 PK")
                        ),
                        responseBody()
                ))
                .andExpect(status().isNoContent());
    }

    private Snippet getPageQuerySnippet() {
        return queryParameters(
                parameterWithName("page").description("페이지 번호"),
                parameterWithName("size").description("페이지당 게시글 수"),
                parameterWithName("sort").description("정렬 기준"));
    }

    private Snippet getPostPageSnippet() {
        return responseFields(
                fieldWithPath("contents").description("게시글 리스트"),
                fieldWithPath("totalPages").description("총 페이지 수"),
                fieldWithPath("size").description("페이지 게시글 수"),
                fieldWithPath("numberOfElements").description("현재 페이지 게시글 수"),
                fieldWithPath("totalElements").description("전체 게시글 수"),
                fieldWithPath("sorted").description("정렬 상태"),

                fieldWithPath("contents.[].pid").description("게시글 PK"),
                fieldWithPath("contents.[].uid").description("유저 PK"),
                fieldWithPath("contents.[].username").description("유저명"),
                fieldWithPath("contents.[].title").description("게시글 제목"),
                fieldWithPath("contents.[].contents").description("게시글 내용"),
                fieldWithPath("contents.[].category").description("카테고리"),
                fieldWithPath("contents.[].updatedAt").description("수정 시간"),
                fieldWithPath("contents.[].createdAt").description("생성 시간"),
                fieldWithPath("contents.[].view").description("조회 수"));
    }

    private Snippet getPostResponseSnippet() {
        return responseFields(
                fieldWithPath("pid").description("게시글 PK"),
                fieldWithPath("uid").description("유저 PK"),
                fieldWithPath("username").description("유저명"),
                fieldWithPath("title").description("게시글 제목"),
                fieldWithPath("contents").description("게시글 내용"),
                fieldWithPath("category").description("카테고리"),
                fieldWithPath("updatedAt").description("수정 시간"),
                fieldWithPath("createdAt").description("생성 시간"),
                fieldWithPath("view").description("조회 수"));
    }

}
