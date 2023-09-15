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
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
        Map<String, String> cookieMap = tokenProvider.resolveToken(request.getCookies());
        String username = principal.getName();
        log.info(username);
        String accessToken = tokenProvider.getAccessToken(username);
        String refreshToken = tokenProvider.getRefreshToken(username);

        Cookie access = cookieProvider.getCookie(JwtProperties.accessTokenName, accessToken,
                ACCESS_TOKEN_EXPIRE);
        Cookie refresh = cookieProvider.getCookie(JwtProperties.refreshTokenName, refreshToken,
                REFRESH_TOKEN_EXPIRE);
        response.addCookie(access);
        response.addCookie(refresh);

        super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    private Map<String, Object> cookie2map(Cookie[] cookies) {
        Map<String, Object> hashMap = new HashMap<>();
        for (Cookie cookie : cookies) {
            hashMap.put(cookie.getName(), cookie.getValue());
        }
        return hashMap;
    }
}
