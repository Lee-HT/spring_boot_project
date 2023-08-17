package com.example.demo.Controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = LoginController.class,
        // Security 예외 처리
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@WithMockUser
class LoginControllerTest {

    private final MockMvc mvc;

    @Autowired
    public LoginControllerTest(MockMvc mockMvc){
        this.mvc = mockMvc;
    }

    @Test
    @DisplayName("Login Test")
    public void login() throws Exception {
        mvc.perform(get("/login.login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }
}