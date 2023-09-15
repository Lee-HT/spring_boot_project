package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
@WithMockUser
public class PostControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @Autowired
    public PostControllerTest(MockMvc mockMvc, PostService postService, ObjectMapper objectMapper) {
        this.mvc = mockMvc;
        this.postService = postService;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getPost() throws Exception {
        when(postService.findPost(any(Pageable.class))).thenReturn(
                PostPageDto.builder().contents(new ArrayList<>()).totalPages(2)
                        .size(3).numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid"))
                        .build());

        mvc.perform(get("/post")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("main/post"));
    }

    @Test
    public void uploadPost() throws Exception {
        mvc.perform(get("/post/write")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("post/write"));
    }

    @Test
    public void savePost() throws Exception {
        PostDto post = PostDto.builder().pid(1L).title("title1").contents("contents1")
                .username("user1").category("category1").build();
        UserDto user = UserDto.builder().uid(1L).username("username1").email("email1@gmail.com").build();
        when(postService.savePost(any(PostDto.class),any(UserEntity.class))).thenReturn(post);

        mvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)).content(objectMapper.writeValueAsString(user)).with(csrf()))
                .andDo(print()).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("post"));
    }

    @Test
    public void searchTitle() throws Exception {
        when(postService.findPostByTitle(any(String.class), any(Pageable.class))).thenReturn(
                PostPageDto.builder().contents(new ArrayList<>()).totalPages(2).size(3)
                        .numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build());

        mvc.perform(get("/post/title/title").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("main/post"));
    }

    @Test
    public void searchUsername() throws Exception {
        when(postService.findPostByUsername(any(String.class), any(Pageable.class))).thenReturn(
                PostPageDto.builder().contents(new ArrayList<>()).totalPages(2).size(3)
                        .numberOfElements(3).sorted(Sort.by(Direction.DESC, "pid")).build());

        mvc.perform(get("/post/username/user").contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andExpect(view().name("main/post"));
    }

    @Test
    public void likePost() throws Exception {
        when(postService.likePost(any(Long.class), any(Long.class))).thenReturn(true);

        mvc.perform(get("/post/like")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void hatePost() throws Exception {
        when(postService.hatePost(any(Long.class), any(Long.class))).thenReturn(true);

        mvc.perform(get("/post/like")).andDo(print()).andExpect(status().isOk());
    }

}
