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

import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.PostService;
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

@WebMvcTest(PostController.class)
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
    public void getPost() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build();
        when(postService.findPost(any(Pageable.class))).thenReturn(postPageDto);

        mvc.perform(get("/post").with(oauth2Login())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.sorted").exists());
    }

    @Test
    public void savePost() throws Exception {
        UserDto user = UserDto.builder().uid(1L).username("username1").email("email1@gmail.com")
                .build();
        PostDto post = PostDto.builder().pid(1L).uid(user.getUid()).title("title1")
                .contents("contents1")
                .username("user1").category("category1").build();
        when(postService.savePost(any(PostDto.class))).thenReturn(post);

        mvc.perform(post("/post").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .content(objectMapper.writeValueAsString(user)).with(csrf())).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void searchTitle() throws Exception {
        PostPageDto postPageDto = PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build();
        when(postService.findPostByTitle(any(String.class), any(Pageable.class))).thenReturn(
                postPageDto);

        mvc.perform(get("/post/title/title").with(oauth2Login())
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
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

        mvc.perform(get("/post/username/user").with(oauth2Login())
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").isArray())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.numberOfElements").exists())
                .andExpect(jsonPath("$.sorted").exists());
    }


}
