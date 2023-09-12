package com.example.demo.Config.Oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class TokenService {

    private final Environment env;

    //    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    public TokenService(Environment env) {
        this.env = env;
    }

    public void socialLogin(String code, String registrationId) {
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        log.info("accessToken = " + accessToken);
        log.info("userResourceNode = " + userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();
        log.info("id = " + id);
        log.info("email = " + email);
        log.info("nickname = " + nickname);
    }

    // 하나의 authorization code 로 토큰 발급 후 그 코드 사용 불가
    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty(
                "spring.security.oauth2.client.registration." + registrationId + ".client-id");
        String clientSecret = env.getProperty(
                "spring.security.oauth2.client.registration." + registrationId + ".client-secret");
        String redirectUri = env.getProperty(
                "spring.security.oauth2.client.registration." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty(
                "spring.security.oauth2.client.registration." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        // restTemplate deprecated 대비
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST,
//                new HttpEntity<>(params,headers), JsonNode.class);
//        JsonNode accessTokenNode = responseNode.getBody();

        // Flux = 0~N개 Mono = 0~1개 subscribe 비동기 toStream 동기
        WebClient webClient = WebClient.builder().baseUrl(tokenUri).build();
        JsonNode accessTokenNode = webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(params).retrieve()
                .bodyToMono(JsonNode.class).flux().toStream().findFirst().orElse(null);

        log.info("webClient : " + accessTokenNode);

        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty(
                "spring.security.oauth2.client.registration." + registrationId + ".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer" + accessToken);
        HttpEntity entity = new HttpEntity(headers);

        WebClient webclient = WebClient.builder().baseUrl(resourceUri).build();
        JsonNode userResource = webclient.get().header("Authorization", "Bearer" + accessToken)
                .retrieve().bodyToMono(JsonNode.class).flux().toStream().findFirst().orElse(null);

//        JsonNode userResource = restTemplate.exchange(resourceUri, HttpMethod.GET, entity,
//                JsonNode.class).getBody();
        return userResource;
    }

}
