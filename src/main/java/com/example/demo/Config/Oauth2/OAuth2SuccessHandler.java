package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final Long REFRESH_TOKEN_EXPIRE = JwtProperties.refreshTime / 1000;

    @Autowired
    private OAuth2SuccessHandler() {

    }

}
