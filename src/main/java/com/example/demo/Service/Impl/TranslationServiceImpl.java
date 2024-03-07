package com.example.demo.Service.Impl;

import com.example.demo.DTO.ContentsDto;
import com.example.demo.Service.TranslationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Transactional
@Service
@Slf4j
public class TranslationServiceImpl implements TranslationService {

    @Override
    public ContentsDto getTranslationContents(String language) {
        String baseUrl = "https://translate.googleapis.com";
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();

        try {
            Map<String, Object> body = new HashMap<>() {{
                put("contents", new ArrayList<>());
                put("sourceLanguageCode", "");
                put("targetLanguageCode", "");
            }};

            Map response = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/projects/{project-id}/locations/{location-id}").build())
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .bodyValue(body).retrieve()
                    .bodyToMono(Map.class)
                    .block();
            assert response != null;
            response.get("translations");

        } catch (Exception e) {
            log.info(e.toString());
        }
        return null;
    }

    private String getUri() {
        return null;
    }
}
