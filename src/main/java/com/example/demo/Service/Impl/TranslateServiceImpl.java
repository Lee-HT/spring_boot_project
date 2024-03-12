package com.example.demo.Service.Impl;

import com.example.demo.DTO.ContentsDto;
import com.example.demo.Service.TranslateService;
import com.google.cloud.translate.v3.DetectLanguageRequest;
import com.google.cloud.translate.v3.DetectLanguageResponse;
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Transactional
@Service
@Slf4j
public class TranslateServiceImpl implements TranslateService {

    private final WebClient webClient;
    @Value("${google.project-id}")
    private final String projectId;
    @Value("${google.location}")
    private final String location; // 작업 실행 Region

    @Autowired
    public TranslateServiceImpl(String projectId, String location) {
        this.projectId = projectId;
        this.location = location;
        String baseUrl = "https://translate.googleapis.com";

        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Override
    public ContentsDto getTranslationContents(String text, String targetLanguage) throws IOException {
        List<String> languages = List.of("ko", "en", "ja", "zh-CN", "zh-TW");

        String sourceLanguage = detectionLanguage(text);
        if (languages.contains(targetLanguage)) {
            if (!Objects.equals(sourceLanguage, targetLanguage)) {
                String transText = translateText(sourceLanguage, targetLanguage, text);
                return ContentsDto.builder().contents(transText).build();
            }
        }

        return ContentsDto.builder().contents(text).build();


    }

    private String detectionLanguage(String text) throws IOException {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, location);

            DetectLanguageRequest request = DetectLanguageRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setContent(text)
                    .build();

            DetectLanguageResponse response = client.detectLanguage(request);
//            log.info("detection result : " + response.getLanguagesList());

            return response.getLanguages(0).getLanguageCode();
        } catch (IOException e) {
            log.info(e.toString());
            return null;
        }
    }

    // 클라이언트 라이브러리
    private String translateText(String sourceLanguage, String targetLanguage, String text) throws IOException {
        try (TranslationServiceClient client = TranslationServiceClient.create()) {
            LocationName parent = LocationName.of(projectId, location);

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setSourceLanguageCode(sourceLanguage)
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(text)
                    .build();

            TranslateTextResponse response = client.translateText(request);
//            log.info("translate result : " + response.getTranslationsList());

            return response.getTranslations(0).getTranslatedText();

        } catch (IOException e) {
            log.info(e.toString());
            return null;
        }
    }

    // REST API
    private String translateTextRest(String sourceLanguage, String targetLanguage, String text) {
        try {
            Map<String, Object> body = new HashMap<>() {{
                put("contents", List.of("구글 번역 사용"));
                put("sourceLanguageCode", "kr");
                put("targetLanguageCode", "en");
            }};

            Map<?, ?> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder.path("/v3/projects/{project-id}:translateText")
                            .build(projectId))
                    .headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .bodyValue(body).retrieve()
                    .bodyToMono(Map.class)
                    .block();
            assert response != null;
            log.info(response.toString());
            Object translate = response.get("translations");
            return translate.toString();

        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }
}
