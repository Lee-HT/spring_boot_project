package com.example.demo.Controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.Config.Doc.RestDocsSetUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
    void HomeTest() throws Exception {
        mvc.perform(get("").with(oauth2Login()))
                .andExpect(status().isOk());
    }

}