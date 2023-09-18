package com.example.demo.Controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.Oauth2CustomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = LoginController.class)
class LoginControllerTest {

    private final MockMvc mvc;
    @MockBean
    private final OAuth2Service oAuth2Service;
    @MockBean
    private final Oauth2CustomService oauth2CustomService;

    @Autowired
    public LoginControllerTest(MockMvc mockMvc, OAuth2Service oAuth2Service,
            Oauth2CustomService oauth2CustomService) {
        this.mvc = mockMvc;
        this.oAuth2Service = oAuth2Service;
        this.oauth2CustomService = oauth2CustomService;
    }

    @Test
    @DisplayName("Login Test")
    public void loginViewTest() throws Exception {
        mvc.perform(get("/login/login").with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/login"));
    }

    @Test
    @DisplayName("Oauth2 Test")
    public void oauth2Test() throws Exception {
        mvc.perform(get("/login/test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}