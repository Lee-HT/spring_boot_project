package com.example.demo.Controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HomeController.class)
//@WithMockUser(roles = "USER")
class HomeControllerTest {

    private final MockMvc mvc;

    @Autowired
    public HomeControllerTest(MockMvc mockMvc){
        this.mvc = mockMvc;
    }

    @Test
    @DisplayName("Home test")
    public void Home_test() throws Exception {
        mvc.perform(get("").with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("main/home"));
    }

}