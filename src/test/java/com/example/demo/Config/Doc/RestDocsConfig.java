package com.example.demo.Config.Doc;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

@TestConfiguration
public class RestDocsConfig {

    private final Environment env;

    public RestDocsConfig(Environment env) {
        this.env = env;
    }

    private OperationPreprocessor preprocessors() {
        return Preprocessors.modifyUris().host(env.getProperty("domain")).port(6550);
    }

    @Bean
    public RestDocumentationResultHandler write() {
        return MockMvcRestDocumentation.document("{class-name}/{method-name}",
                // request
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint(),preprocessors()),
                // response
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint(),preprocessors())
        );
    }

}
