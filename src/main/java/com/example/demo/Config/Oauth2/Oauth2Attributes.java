package com.example.demo.Config.Oauth2;

import com.example.demo.Entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Builder
@Getter
public class Oauth2Attributes {
    private String registrationId;
    private String attributeKey;
    private String provider;
    private String username;
    private String email;

    public static Oauth2Attributes of(String registrationId, String usernameAttributeName,
                                      Map<String, Object> attributes) {
        // provider 구별
        if (registrationId.equals("google")) {
            return ofGoogle(registrationId, usernameAttributeName, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(registrationId, attributes);
        } else {
            throw new OAuth2AuthenticationException("허용 불가 인증");
        }
    }

    public static Oauth2Attributes ofGoogle(String registrationId, String usernameAttributeName, Map<String, Object> attributes) {
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributeKey((String) attributes.get(usernameAttributeName))
                .provider(registrationId + "_" + attributes.get(usernameAttributeName)).build();
    }

    public static Oauth2Attributes ofNaver(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> attribute = (Map<String, Object>) attributes.get("response");
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username((String) attribute.get("nickname"))
                .email((String) attribute.get("email"))
                .attributeKey((String) attribute.get("id"))
                .provider(registrationId + "_" + attributes.get("id")).build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder().username(username).email(email).provider(provider)
                .roles("ROLE_USER").build();
    }

    public String toString() {
        return toMap().toString();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("registrationId", registrationId);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("attributeKey", attributeKey);
        hashMap.put("provider", provider);
        return hashMap;
    }

}
