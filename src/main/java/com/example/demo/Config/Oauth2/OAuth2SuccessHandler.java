package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Cookie.CookieProvider;
import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public final class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieProvider cookieProvider;
    private final TokenProvider tokenProvider;
    private final Environment env;
    private final int REFRESH_TOKEN_EXPIRE = (int) (JwtProperties.refreshTime / 1000);

    @Autowired
    public OAuth2SuccessHandler(CookieProvider cookieProvider, TokenProvider tokenProvider,
            Environment env) {
        this.cookieProvider = cookieProvider;
        this.tokenProvider = tokenProvider;
        this.env = env;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("get Id : " + ((DefaultOAuth2User) authentication.getPrincipal()).getName());
        Map<String, Object> attributes = ((DefaultOAuth2User) authentication.getPrincipal()).getAttributes();
        log.info("SuccessHandler attribute : " + attributes.toString());
        String username = (String) attributes.get("username");
        String provider = (String) attributes.get("provider");
        String refreshToken = tokenProvider.getRefreshToken(username, provider);

        Cookie refresh = cookieProvider.getCookie(JwtProperties.refreshTokenName, JwtProperties.refreshTokenPath,
                refreshToken, REFRESH_TOKEN_EXPIRE);
        response.addCookie(refresh);

        setDefaultTargetUrl(getTargetUrl());
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    private String getTargetUrl() {
        String path = "/codelia_react/oauth2/redirect";
        String host = env.getProperty("front");
        return UriComponentsBuilder.fromUriString(host + path).build().toString();
    }
}
