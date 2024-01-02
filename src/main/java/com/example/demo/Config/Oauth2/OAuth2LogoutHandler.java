package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Cookie.CookieProvider;
import com.example.demo.Config.Jwt.JwtProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final CookieProvider cookieProvider;

    public OAuth2LogoutHandler(CookieProvider cookieProvider) {
        this.cookieProvider = cookieProvider;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Cookie accessToken = cookieProvider.expireCookie(JwtProperties.accessTokenName);
        Cookie refreshToken = cookieProvider.expireCookie(JwtProperties.refreshTokenName);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        super.onLogoutSuccess(request, response, authentication);
    }
}
