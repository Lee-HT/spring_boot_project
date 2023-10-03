package com.example.demo.Config.Jwt;

import com.example.demo.Config.Cookie.CookieProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final CookieProvider cookieProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, CookieProvider cookieProvider) {
        this.tokenProvider = tokenProvider;
        this.cookieProvider = cookieProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> tokens = tokenProvider.resolveToken(request.getCookies());
        String accessToken = tokens.get(JwtProperties.accessTokenName);
        String refreshToken = tokens.get(JwtProperties.refreshTokenName);

        if (!tokens.isEmpty()) {
            // AccessToken 유효 시 SecurityContext 에 Authentication 저장
            if (tokenProvider.validationToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            // RefreshToken 유효 시 accessToken 재발급
            } else if (tokenProvider.validationToken(refreshToken)) {
                String newAccessToken = tokenProvider.getAccessToken(
                        tokenProvider.getUsername(refreshToken),tokenProvider.getProvider(refreshToken));
                response.addCookie(
                        cookieProvider.getCookie(JwtProperties.accessTokenName, newAccessToken,
                                (int) (JwtProperties.accessTime / 1000)));

                Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
