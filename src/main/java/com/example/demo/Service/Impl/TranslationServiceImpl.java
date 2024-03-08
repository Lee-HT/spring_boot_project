package com.example.demo.Service.Impl;

import com.example.demo.DTO.ContentsDto;
import com.example.demo.Service.TranslationService;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public ContentsDto getTranslationContents(String text) throws IOException {
        List<String> languages = List.of("ko", "en", "ja", "zh-CN", "zh-TW");
        String projectId = "spring-blog-397204";
        String sourceLanguage = "ko";
        String targetLanguage = "en";
        String transText = translateText(projectId, sourceLanguage, targetLanguage, text);

        return ContentsDto.builder().contents(transText).build();
    }

    private String translateText(String projectId, String sourceLanguage, String targetLanguage, String text)
            throws IOException {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            // 작업 실행 region
//            String location = "asia-northeast3";
            String location = "global";
            LocationName parent = LocationName.of(projectId, location);

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setSourceLanguageCode(sourceLanguage)
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(text)
                    .build();
            TranslateTextResponse response = client.translateText(request);

            log.info(response.getTranslationsList().toString());

            return response.getTranslations(0).getTranslatedText();

//            for (Translation translation : response.getTranslationsList()) {
//                translation.getTranslatedText();
//            }
        } catch (IOException e) {
            log.info(e.toString());
            return null;
        }
    }

    private void translateTextRest() {
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
    }
}
