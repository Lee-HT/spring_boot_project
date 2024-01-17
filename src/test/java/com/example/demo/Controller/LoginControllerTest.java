package com.example.demo.Controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.Oauth2CustomService;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = LoginController.class)
class LoginControllerTest extends RestDocsSetUp {

    @MockBean
    private final OAuth2Service oAuth2Service;
    @MockBean
    private final UserService userService;
    @MockBean
    private final Oauth2CustomService oauth2CustomService;

    @Autowired
    public LoginControllerTest(RestDocumentationResultHandler restDocs, MockMvc mockMvc,
            OAuth2Service oAuth2Service,
            UserService userService, Oauth2CustomService oauth2CustomService) {
        super(restDocs, mockMvc);
        this.userService = userService;
        this.oAuth2Service = oAuth2Service;
        this.oauth2CustomService = oauth2CustomService;
    }

    @Test
    @DisplayName("Google Oauth2 Test")
    void oauth2Test() throws Exception {
        mvc.perform(get("/login/test/google").with(oauth2Login()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                        "https://accounts.google.com/o/oauth2/v2/auth?client_id=241034246573-r8a6mk2a53s9biah83n1hklutsrqni51.apps.googleusercontent.com"
                                +
                                "&redirect_uri=http://localhost:6550/login/oauth2/test/google&response_type=code&scope=email%20profile"));
    }

    @Test
    @DisplayName("userInfo Test")
    void getUserInfo() throws Exception {
        when(userService.findByProvider()).thenReturn(
                UserDto.builder().uid(1L).username("user1").email("email@gmail.com")
                        .profilePic("imgLink").build());
        mvc.perform(get("/oauth2/userinfo").with(oauth2Login()))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("uid").description("유저 PK"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("profilePic").description("프로필 이미지")
                        )
                ))
                .andDo(print())
                .andExpect(status().isOk());

        when(userService.findByProvider()).thenReturn(UserDto.builder().build());
        mvc.perform(get("/oauth2/userinfo").with(oauth2Login()))
                .andDo(print())
                .andExpect(status().isNoContent());


    }
}