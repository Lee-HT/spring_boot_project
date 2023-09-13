package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.Impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@WithMockUser
public class UserControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private UserServiceImpl userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getUser() throws Exception {
        UserDto user = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1")
                .build();
        when(userService.findByUsername(any(String.class))).thenReturn(user);

        mvc.perform(get("/user/myPage").queryParam("username", "user1")).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("user/myPage"));
    }

    @Test
    public void saveUser() throws Exception {
        UserDto dto = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1")
                .build();
        when(userService.saveUser(any(UserDto.class))).thenReturn(dto);

        mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(csrf())).andDo(print())
                .andExpect(status().isOk()).andExpect(view().name("user/info"));
    }

}
