package com.example.demo.Controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TestController.class)
@AutoConfigureRestDocs
//@WithMockUser(roles = "USER")
class TestControllerTest extends RestDocsSetUp {

    @Autowired
    public TestControllerTest(RestDocumentationResultHandler restDocs, MockMvc mvc){
        super(restDocs,mvc);
    }

    @Test
    @DisplayName("HomeTest")
    public void HomeTest() throws Exception {
        mvc.perform(RestDocumentationRequestBuilders.get("").with(oauth2Login()))
                .andDo(restDocs.document())
                .andExpect(status().isOk());
    }

}