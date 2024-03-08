package com.example.demo.Service;

import com.example.demo.Service.Impl.TranslationServiceImpl;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {

    @InjectMocks
    private TranslationServiceImpl translationService;

    @Test
    void getTranslationContents() throws IOException {
        System.out.println(translationService.getTranslationContents("texts").getContents());
    }
}
