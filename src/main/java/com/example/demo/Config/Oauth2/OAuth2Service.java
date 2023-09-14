package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Jwt.TokenProvider;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.UserRepository;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public OAuth2Service(TokenProvider tokenProvider, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser");
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        // provider 구별
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // get attributes
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info(attributes.toString());

        Oauth2Attributes oauth2Attributes = Oauth2Attributes.of(registrationId,
                userNameAttributeName, attributes);

        UserEntity user = saveUser(oauth2Attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoles())), attributes,
                userNameAttributeName);
    }

    private UserEntity saveUser(Oauth2Attributes oauth2Attributes) {
        UserEntity user = userRepository.findByEmail(oauth2Attributes.getEmail());
        if (user != null) {
            user.updateUsername(oauth2Attributes.getUsername());
        } else {
            user = userRepository.save(oauth2Attributes.toEntity());
        }
        return user;
    }
}
