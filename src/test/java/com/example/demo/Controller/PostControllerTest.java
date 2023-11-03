package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
class PostControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final PostService postService;

    @Autowired
    public PostControllerTest(MockMvc mockMvc, PostService postService, ObjectMapper objectMapper) {
        this.mvc = mockMvc;
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getPostPage() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build();
        when(postService.findPost(any(Pageable.class))).thenReturn(postPageDto);

        mvc.perform(RestDocumentationRequestBuilders.get("/post?page=0&size=10&sort=pid")
                        .with(oauth2Login()))
                .andDo(print())
                .andDo(document("post_controller_test/get_post_page",
                        queryParameters(
                                parameterWithName("page").description("page"),
                                parameterWithName("size").description("size"),
                                parameterWithName("sort").description("sort")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("contents"),
                                fieldWithPath("totalPages").description("totalPages"),
                                fieldWithPath("size").description("size"),
                                fieldWithPath("numberOfElements").description("numberOfElements"),
                                fieldWithPath("sorted.empty").description("empty"),
                                fieldWithPath("sorted.sorted").description("sorted.sorted"),
                                fieldWithPath("sorted.unsorted").description("unsorted")
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
    public void savePost() throws Exception {
        PostDto post = PostDto.builder().pid(1L).uid(1L).title("title1")
                .contents("contents1")
                .username("user1").category("category1").build();
        when(postService.savePost(any(PostDto.class))).thenReturn(post);

        mvc.perform(RestDocumentationRequestBuilders.post("/post").with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)).with(csrf())).andDo(print())
                .andDo(document("post_controller_test/save_post",
                        requestFields(
                                fieldWithPath("pid").description("pid"),
                                fieldWithPath("uid").description("uid"),
                                fieldWithPath("title").description("title"),
                                fieldWithPath("contents").description("contents"),
                                fieldWithPath("username").description("username"),
                                fieldWithPath("category").description("category"),
                                fieldWithPath("updatedAt").ignored()
                        ),
                        responseFields(
                                fieldWithPath("pid").description("pid"),
                                fieldWithPath("uid").description("uid"),
                                fieldWithPath("title").description("title"),
                                fieldWithPath("contents").description("contents"),
                                fieldWithPath("username").description("username"),
                                fieldWithPath("category").description("category"),
                                fieldWithPath("updatedAt").type("LocalDateTime")
                                        .description("updatedAt")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").exists());
    }

    @Test
    public void searchTitle() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build();
        when(postService.findPostByTitle(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(RestDocumentationRequestBuilders.get("/post/title/{title}?page=0&size=10&sort=pid", "title")
                        .with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andDo(document("post_controller_test/get_post_title",
                        pathParameters(
                                parameterWithName("title").description("title")
                        ),
                        queryParameters(
                                parameterWithName("page").description("page"),
                                parameterWithName("size").description("size"),
                                parameterWithName("sort").description("sort")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("contents"),
                                fieldWithPath("totalPages").description("totalPages"),
                                fieldWithPath("size").description("size"),
                                fieldWithPath("numberOfElements").description("numberOfElements"),
                                fieldWithPath("sorted.empty").description("empty"),
                                fieldWithPath("sorted.sorted").description("sorted.sorted"),
                                fieldWithPath("sorted.unsorted").description("unsorted")
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
    public void searchUsername() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build();
        when(postService.findPostByUsername(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(MockMvcRequestBuilders.get("/post/username/user").with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.sorted").exists());
    }


}
