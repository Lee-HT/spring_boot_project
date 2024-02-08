package com.example.demo.Config.Cookie;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieProvider {

    private Cookie cookie;
    private ResponseCookie responseCookie;
    private final Environment env;

    public CookieProvider(Environment env) {
        this.env = env;
    }

    public void setCookie(String name, String path) {
        this.cookie = new Cookie(name, null);
        this.cookie.setHttpOnly(true);
        this.cookie.setSecure(true);
        this.cookie.setDomain(env.getProperty("domain"));
        this.cookie.setPath(path);
    }

    public Cookie getCookie(String name, String path, String value, int expire) {
        setCookie(name, path);
        this.cookie.setValue(value);
        this.cookie.setMaxAge(expire);

        return this.cookie;
    }

    public String getResponseCookie(String name, String path, String value, int expire) {
        responseCookie = ResponseCookie.from(name, value).path(path).httpOnly(true).secure(true).sameSite("None")
                .maxAge(expire).build();
        return responseCookie.toString();
    }

    public Cookie expireCookie(String name, String path) {
        setCookie(name, path);
        this.cookie.setMaxAge(0);

        return this.cookie;
    }


}
