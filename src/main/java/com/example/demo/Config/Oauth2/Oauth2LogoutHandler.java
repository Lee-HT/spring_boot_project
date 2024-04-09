package com.example.demo.Config.Oauth2;

import io.jsonwebtoken.lang.Assert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Oauth2LogoutHandler implements LogoutHandler {
    private final List<String> cookiesClear;

    public Oauth2LogoutHandler(String... cookies){
        Assert.notNull(cookies,"쿠키 null 불가능");
        this.cookiesClear = new ArrayList<>(Arrays.asList(cookies));
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        this.cookiesClear.forEach((cookie) -> response.addHeader("Set-Cookie",cookie));
    }
}
