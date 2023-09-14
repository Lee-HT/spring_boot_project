package com.example.demo.Config.Oauth2;

import com.example.demo.Entity.UserEntity;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Builder
@Getter
public class Oauth2Attributes {

    private String registrationId;
    private String attributeKey;
    private String username;
    private String email;

    public static Oauth2Attributes of(String registrationId, String usernameAttributeName,
            Map<String, Object> attributes) {
        // provider 구별
        if (registrationId.equals("google")) {
            return ofGoogle(registrationId, usernameAttributeName, attributes);
        } else if (registrationId.equals("naver")) {
            return ofNaver(registrationId, usernameAttributeName, attributes);
        } else {
            throw new OAuth2AuthenticationException("허용 불가 인증");
        }
    }

    public static Oauth2Attributes ofGoogle(String registrationId, String usernameAttributeName,
            Map<String, Object> attributes) {
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username((String) attributes.get("username"))
                .email((String) attributes.get("email"))
                .attributeKey((String) attributes.get(usernameAttributeName)).build();
    }

    public static Oauth2Attributes ofNaver(String registrationId, String usernameAttributeName,
            Map<String, Object> attributes) {
        Map<String,Object> attribute = (Map<String, Object>) attributes.get("response");
        return Oauth2Attributes.builder().registrationId(registrationId)
                .username((String) attribute.get("nickname"))
                .email((String) attribute.get("email"))
                .attributeKey((String) attribute.get("id")).build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder().username(username).email(email).roles("ROLE_USER").build();
    }

}
