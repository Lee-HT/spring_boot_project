package com.example.demo.Service.Impl;

import com.example.demo.Config.Jwt.TokenProvider;
import com.example.demo.Service.LoginService;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LoginServiceImpl implements LoginService {

    private final TokenProvider tokenProvider;

    public LoginServiceImpl(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String getAccessToken(Cookie cookie) {
        String refreshToken = tokenProvider.resolveCookie(cookie);
        if (!refreshToken.isBlank() && tokenProvider.validationToken(refreshToken)) {
            return tokenProvider.getAccessToken(
                    tokenProvider.getUsername(refreshToken),
                    tokenProvider.getProvider(refreshToken));
        }
        return "";
    }
}
