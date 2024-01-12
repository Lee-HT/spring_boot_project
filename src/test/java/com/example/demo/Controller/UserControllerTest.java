package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.Impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private final UserServiceImpl userService;

    @Autowired
    public UserControllerTest(RestDocumentationResultHandler restDocs, MockMvc mockMvc,
            ObjectMapper objectMapper, UserServiceImpl userService) {
        super(restDocs, mockMvc);
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Test
    public void updateUser() throws Exception {
        UserDto dto = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1")
                .build();
        when(userService.updateUser(any(UserDto.class))).thenReturn(dto);

        mvc.perform(put("/user").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(csrf()))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("uid").optional().description("유저 PK"),
                                fieldWithPath("email").description("유저 이메일"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("profilePic").description("프로필 사진")
                        ),
                        responseFields(
                                fieldWithPath("uid").description("유저 PK"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("email").description("유저 이메일"),
                                fieldWithPath("profilePic").description("프로필 사진")
                        )
                ))
                .andExpect(status().isOk());
    }

    @Test
    public void getUser() throws Exception {
        UserDto dto = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1")
                .build();
        when(userService.findByProvider()).thenReturn(dto);

        mvc.perform(get("/user/{uid}", 1L).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("uid").description("유저 PK")
                        ),
                        responseFields(
                                fieldWithPath("uid").description("유저 PK"),
                                fieldWithPath("username").description("유저명"),
                                fieldWithPath("email").description("유저 이메일"),
                                fieldWithPath("profilePic").description("프로필 사진"),
                                fieldWithPath("createdAt").description("생성 시간")
                        )
                ))
                .andExpect(jsonPath("$uid").isNumber())
                .andExpect(jsonPath("username").exists())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        when(userService.deleteUser()).thenReturn(1L);

        mvc.perform(delete("/user").with(oauth2Login()))
                .andDo(restDocs.document(responseFields(
                                fieldWithPath("uid").description("유저 PK")
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$uid").isNumber());
    }

}
