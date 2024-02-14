package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Oauth2.Oauth2CustomService;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.LoginService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = LoginController.class)
class LoginControllerTest extends RestDocsSetUp {

    @MockBean
    private UserService userService;
    @MockBean
    private LoginService loginService;
    @MockBean
    private Oauth2CustomService oauth2CustomService;

    @Autowired
    public LoginControllerTest(RestDocumentationResultHandler restDocs, MockMvc mockMvc) {
        super(restDocs, mockMvc);
    }

    @Test
    void oauth2RedirectTest() throws Exception {
        mvc.perform(get("/login/test/google").with(oauth2Login()))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getUserInfo() throws Exception {
        when(userService.findByProvider()).thenReturn(
                UserDto.builder().uid(1L).username("user").email("email").profilePic("link")
                        .build());
        mvc.perform(get("/oauth2/userinfo").with(oauth2Login())
                        .header(JwtProperties.headerName, "bearer-"))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("uid").description("유저 PK"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("profilePic").description("프로필 이미지"))
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getAccessToken() throws Exception {
        when(loginService.getAccessToken(any(Cookie.class))).thenReturn("bearer-");
        mvc.perform(post("/oauth2/token").with(oauth2Login())
                        .cookie(new Cookie(JwtProperties.refreshTokenName, "token")))
                .andDo(restDocs.document())
                .andExpect(status().isCreated());
    }
}