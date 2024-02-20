package com.example.demo.Controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import com.example.demo.DTO.CategoryDto;
import com.example.demo.Service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest extends RestDocsSetUp {

    private final ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;

    @Autowired
    CategoryControllerTest(RestDocumentationResultHandler restDocs, MockMvc mvc, ObjectMapper objectMapper) {
        super(restDocs, mvc);
        this.objectMapper = objectMapper;
    }

    @Test
    void getCategory() throws Exception {
        List<CategoryDto> response = List.of(CategoryDto.builder().id(1L).parent("parent").name("name").build());
        when(categoryService.getCategory()).thenReturn(response);
        mvc.perform(get("/category").with(oauth2Login()))
                .andDo(restDocs.document(
                        getResponseCategoryList()
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryGroup() throws Exception {
        Map<String, Map<String, ?>> response = new HashMap<>();
        response.put("contents", Map.of("parent", List.of("name1", "name2")));
        when(categoryService.getCategoryGroup()).thenReturn(response);
        mvc.perform(get("/category/parent").with(oauth2Login()))
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("contents").description("Key 값: 상위 카테고리"),
                                fieldWithPath("contents.*.[]").description("해당 분류의 카테고리"))
                ))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryByParent() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder().id(1L).parent("parent").name("name").build();
        when(categoryService.getCategoryByParent(anyString())).thenReturn(Collections.singletonList(categoryDto));
        mvc.perform(get("/category/parent/{parent}", "parent").with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("parent").description("카테고리 분류")),
                        getResponseCategoryList()
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void saveCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto.builder().id(1L).parent("parent").name("name").build();
        when(categoryService.saveCategory(any(CategoryDto.class))).thenReturn(categoryDto);
        mvc.perform(post("/category").with(oauth2Login()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("id").ignored(),
                                fieldWithPath("parent").description("상위 카테고리"),
                                fieldWithPath("name").description("카테고리 이름"))
                ))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteCategory() throws Exception {
        when(categoryService.deleteCategory(anyLong())).thenReturn(1L);
        mvc.perform(delete("/category/{id}", 1L).with(oauth2Login()))
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("id").description("카테고리 PK"))
                ))
                .andExpect(status().isNoContent());
    }

    private Snippet getResponseCategoryList() {
        return responseFields(
                fieldWithPath("[].id").description("카테고리 PK"),
                fieldWithPath("[].parent").description("상위 카테고리"),
                fieldWithPath("[].name").description("카테고리 이름"));
    }
}
