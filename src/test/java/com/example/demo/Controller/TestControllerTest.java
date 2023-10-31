package com.example.demo.Controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TestController.class)
@AutoConfigureRestDocs
//@WithMockUser(roles = "USER")
class TestControllerTest {

    private final MockMvc mvc;

    @Autowired
    public TestControllerTest(MockMvc mockMvc){
        this.mvc = mockMvc;
    }

    @Test
    @DisplayName("HomeTest")
    public void HomeTest() throws Exception {
        mvc.perform(get("").with(oauth2Login()))
                .andDo(print())
                .andDo(
                        document("HomeTest")
                )
                .andExpect(status().isOk());
    }

}