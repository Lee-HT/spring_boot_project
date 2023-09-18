package com.example.demo.Config.Oauth2;

import com.example.demo.Config.Cookie.CookieProvider;
import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Jwt.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
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
            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        log.info("successHandler : " + username);
        String accessToken = tokenProvider.getAccessToken(username);
        String refreshToken = tokenProvider.getRefreshToken(username);

        Cookie access = cookieProvider.getCookie(JwtProperties.accessTokenName, accessToken,
                ACCESS_TOKEN_EXPIRE);
        Cookie refresh = cookieProvider.getCookie(JwtProperties.refreshTokenName, refreshToken,
                REFRESH_TOKEN_EXPIRE);
        response.addCookie(access);
        response.addCookie(refresh);

        String targetUrl = getTargetUrl(request.getRequestURI(), accessToken, refreshToken);
        log.info(targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

//        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    private String getTargetUrl(String url, String accessToken, String refreshToken) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam(JwtProperties.accessTokenName, accessToken)
                .queryParam(JwtProperties.accessTokenName, refreshToken)
                .build().toString();
    }
}
