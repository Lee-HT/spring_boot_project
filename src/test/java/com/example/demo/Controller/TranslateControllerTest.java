package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.ContentsDto;
import com.example.demo.DTO.TranslateTextDto;
import com.example.demo.Service.TranslateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TranslateController.class)
class TranslateControllerTest extends RestDocsSetUp {

    @MockBean
    private final TranslateService translateService;
    private final ObjectMapper objectMapper;

    @Autowired
    TranslateControllerTest(RestDocumentationResultHandler restDocs, MockMvc mvc, TranslateService translateService,
            ObjectMapper objectMapper) {
        super(restDocs, mvc);
        this.translateService = translateService;
        this.objectMapper = objectMapper;
    }

    @Test
    void getTranslateText() throws Exception {
        TranslateTextDto dto = TranslateTextDto.builder().text("구글 번역 테스트").targetLanguage("en").build();
        when(translateService.getTranslationContents(anyString(), anyString())).thenReturn(
                ContentsDto.builder().contents("google translate test").build());

        mvc.perform(post("/translate").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("text").description("번역 할 텍스트"),
                                fieldWithPath("targetLanguage").description("번역 언어")
                        ),
                        responseFields(
                                fieldWithPath("contents").description("번역 완료 텍스트")
                        )
                ))
                .andExpect(status().isCreated());
    }
}