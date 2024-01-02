package com.example.demo.Config.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("JwtAuthenticationFilter");
            log.info(request.getHeader("Authorization"));
            String accessToken = tokenProvider.resolveToken(request.getHeader("Authorization"));
            log.info("accessToken : " + accessToken);
            // AccessToken 유효 시 SecurityContext 에 Authentication 저장
            if (!accessToken.isBlank() && tokenProvider.validationToken(accessToken)) {
                Authentication authentication = tokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                String refreshToken = tokenProvider.resolveCookie(request.getCookies(),
                        JwtProperties.refreshTokenName);
                // RefreshToken 유효 시 accessToken 재발급 후 Authentication 저장
                if (!refreshToken.isBlank() && tokenProvider.validationToken(refreshToken)) {
                    String newAccessToken = tokenProvider.getAccessToken(
                            tokenProvider.getUsername(refreshToken),
                            tokenProvider.getProvider(refreshToken));
                    response.addHeader("Authorization", newAccessToken);

                    Authentication authentication = tokenProvider.getAuthentication(
                            refreshToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info(String.valueOf(e));
            filterChain.doFilter(request, response);
        }

    }
}
