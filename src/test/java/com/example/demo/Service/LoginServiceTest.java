package com.example.demo.Service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Jwt.TokenProvider;
import com.example.demo.Service.Impl.LoginServiceImpl;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @InjectMocks
    private LoginServiceImpl loginService;
    @Mock
    private TokenProvider tokenProvider;
    @Test
    void getAccessToken() {
        when(tokenProvider.resolveCookie(any(Cookie.class))).thenReturn("refreshToken");
        when(tokenProvider.validationToken(anyString())).thenReturn(true);
        when(tokenProvider.getAccessToken(anyString(),anyString())).thenReturn("accessToken");
        when(tokenProvider.getUsername(anyString())).thenReturn("user");
        when(tokenProvider.getProvider(anyString())).thenReturn("google_1");

        String result = loginService.getAccessToken(new Cookie(JwtProperties.refreshTokenName,"refreshToken"));
        Assertions.assertThat(result).isEqualTo("accessToken");
    }

}
