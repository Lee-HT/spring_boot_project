package com.example.demo.Controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ApiController.class)
@AutoConfigureRestDocs
//@WithMockUser(roles = "USER")
class ApiControllerTest extends RestDocsSetUp {

    @MockBean
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public ApiControllerTest(RestDocumentationResultHandler restDocs, MockMvc mvc) {
        super(restDocs, mvc);
    }

    @Test
    void apiDocsRedirect() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}