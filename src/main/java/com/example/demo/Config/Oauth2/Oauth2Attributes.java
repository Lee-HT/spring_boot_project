package com.example.demo.Config.Oauth2;

import com.example.demo.Entity.UserEntity;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Builder
@Getter
public final class Oauth2Attributes {

    private String registrationId;
    private String attributeKey;
    private String provider;
    private String username;
    private String email;
    private String profileImg;

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

    public static Oauth2Attributes ofGoogle(String registrationId, String usernameAttributeName,
            Map<String, Object> attributes) {
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .profileImg(attributes.get("picture").toString())
                .attributeKey(attributes.get(usernameAttributeName).toString())
                .provider(registrationId + "_" + attributes.get(usernameAttributeName)).build();
    }

    public static Oauth2Attributes ofNaver(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> attribute = (Map<String, Object>) attributes.get("response");
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username(attributes.get("name").toString())
                .email(attributes.get("email").toString())
                .profileImg(attributes.get("picture").toString())
                .attributeKey(attribute.get("id").toString())
                .provider(registrationId + "_" + attribute.get("id")).build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder().username(username).email(email).provider(provider).profilePic(profileImg)
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
        hashMap.put("profilePic",profileImg);
        hashMap.put("attributeKey", attributeKey);
        hashMap.put("provider", provider);
        return hashMap;
    }

}
