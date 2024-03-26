package com.example.demo.Service;

import com.example.demo.Service.Impl.TranslateServiceImpl;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class TranslateServiceTest {

    @InjectMocks
    private TranslateServiceImpl translationService;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(translationService, "projectId", "spring-blog-397204");
        ReflectionTestUtils.setField(translationService, "location", "global");
    }

    @Test
    void getTranslationContents() throws IOException {
        String response = translationService.getTranslationContents("구글 번역 기능", "en").getContents();

        System.out.println(response);

        Assertions.assertThat(response).isInstanceOf(String.class);
    }
}
