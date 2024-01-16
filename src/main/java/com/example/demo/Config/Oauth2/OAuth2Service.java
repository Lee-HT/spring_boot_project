package com.example.demo.Config.Oauth2;

import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Autowired
    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // AccessToken 발급 후 userRequest 에 담아 loadUser 에서 user info uri 에 request
        log.info("loadUser");
        // provider 구별
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 각 서비스의 유니크 필드 명 (unique id 값 전달을 위한 키)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // get attributes
        Map<String, Object> attributes = getAttributes(userRequest);
        log.info("attributes : " + attributes.toString());

        Oauth2Attributes oauth2Attributes = Oauth2Attributes.of(registrationId,
                userNameAttributeName, attributes);
        log.info("Oauth2Attributes : " + oauth2Attributes.toString());

        UserEntity user = saveUser(oauth2Attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoles())), oauth2Attributes.toMap(),
                "provider");
    }

    // (WebClient) get user info
    private Map<String, Object> getAttributes(OAuth2UserRequest userRequest) {
        String uri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                .getUri();
        String accessToken = userRequest.getAccessToken().getTokenValue();
        HttpMethod httpMethod = getHttpMethod(userRequest.getClientRegistration());
        WebClient webClient = WebClient.builder().baseUrl(uri).build();

        Map<String, Object> request;
        if (HttpMethod.POST.equals(httpMethod)) {
            request = webClient.post().headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.set(OAuth2ParameterNames.ACCESS_TOKEN, accessToken);
                    })
                    .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    }).flux().toStream().findFirst()
                    .orElse(null);
        } else {
            request = webClient.get().headers(httpHeaders -> {
                        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                        httpHeaders.setBearerAuth(accessToken);
                    })
                    .retrieve().bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    }).flux().toStream().findFirst()
                    .orElse(null);
        }
        return request;
    }

    // DB에 유저 정보 삽입
    private UserEntity saveUser(Oauth2Attributes oauth2Attributes) {
        // naver email 은 고유하지 않음
        Optional<UserEntity> user = userRepository.findByProvider(oauth2Attributes.getProvider());
        if (user.isPresent()) {
            user.get().updateUsername(oauth2Attributes.getUsername());
        } else {
            return userRepository.save(oauth2Attributes.toEntity());
        }
        return user.get();
    }

    // ClientRegistration AuthenticationMethod -> HttpMethod
    private HttpMethod getHttpMethod(ClientRegistration clientRegistration) {
        if (AuthenticationMethod.FORM.equals(
                clientRegistration.getProviderDetails().getUserInfoEndpoint()
                        .getAuthenticationMethod())) {
            return HttpMethod.POST;
        }
        return HttpMethod.GET;
    }

    // DefaultOauth2UserService 에서 user-info 가져옴
//    private Map<String, Object> getAttributes(OAuth2UserRequest userRequest) {
//        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
//
//        return oAuth2User.getAttributes();
//    }

    // DefaultOauth2UserService
//    private Map<String, Object> getAttributes(OAuth2UserRequest userRequest) {
//        // requestEntityConverter
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        String accessToken = userRequest.getAccessToken().getTokenValue();
//        String uri = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
//                .getUri();
//
//        RequestEntity<?> request = null;
//        if (HttpMethod.POST.equals(getHttpMethod(userRequest.getClientRegistration()))) {
//            MultiValueMap<String, String> formParameters = new LinkedMultiValueMap<>();
//            formParameters.add(OAuth2ParameterNames.ACCESS_TOKEN, accessToken);
//            request = new RequestEntity<>(formParameters,headers,
//                    getHttpMethod(userRequest.getClientRegistration()), URI.create(uri));
//        }else {
//            headers.setBearerAuth(accessToken);
//            request = new RequestEntity<>(headers,
//                    getHttpMethod(userRequest.getClientRegistration()), URI.create(uri));
//        }
//        log.info("RequestEntity : " + request.toString());
//
//        ResponseEntity<Map<String, Object>> response = new RestTemplate().exchange(request,
//                new ParameterizedTypeReference<Map<String, Object>>() {});
//        return response.getBody();
//    }

}
