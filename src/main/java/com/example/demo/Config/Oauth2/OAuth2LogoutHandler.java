package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Cookie.CookieProvider;
import com.example.demo.Config.Jwt.JwtProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final CookieProvider cookieProvider;
    private final Environment env;

    public OAuth2LogoutHandler(CookieProvider cookieProvider, Environment env) {
        this.cookieProvider = cookieProvider;
        this.env = env;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Cookie refreshToken = cookieProvider.expireCookie(JwtProperties.refreshTokenName);
        response.addCookie(refreshToken);

        String redirectUrl = env.getProperty("front") + "/codelia_react/";
        if (!response.isCommitted()) {
            String encodedUrl = response.encodeRedirectURL(redirectUrl);
            response.sendRedirect(encodedUrl);
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
