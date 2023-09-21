package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Cookie.CookieProvider;
import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieProvider cookieProvider;
    private final TokenProvider tokenProvider;
    private final int REFRESH_TOKEN_EXPIRE = (int) (JwtProperties.refreshTime / 1000);
    private final int ACCESS_TOKEN_EXPIRE = (int) (JwtProperties.accessTime / 1000);

    @Autowired
    public OAuth2SuccessHandler(CookieProvider cookieProvider, TokenProvider tokenProvider) {
        this.cookieProvider = cookieProvider;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("successHandler");
        String accessToken = tokenProvider.getAccessToken("access");
        String refreshToken = tokenProvider.getRefreshToken("refresh");

        Cookie access = cookieProvider.getCookie(JwtProperties.accessTokenName, accessToken,
                ACCESS_TOKEN_EXPIRE);
        Cookie refresh = cookieProvider.getCookie(JwtProperties.refreshTokenName, refreshToken,
                REFRESH_TOKEN_EXPIRE);
        response.addCookie(access);
        response.addCookie(refresh);

        String targetUrl = getTargetUrl("/", accessToken, refreshToken);
        log.info(targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String getTargetUrl(String url, String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam(JwtProperties.accessTokenName, accessToken)
                .queryParam(JwtProperties.accessTokenName, refreshToken)
                .build().toString();
    }
}
