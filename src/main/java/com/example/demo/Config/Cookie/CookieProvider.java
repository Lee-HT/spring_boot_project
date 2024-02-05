package com.example.demo.Config.Cookie;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CookieProvider {
    private Cookie cookie;
    private final Environment env;

    public CookieProvider(Environment env) {
        this.env = env;
    }

    public void setCookie(String name){
        this.cookie = new Cookie(name,null);
        this.cookie.setHttpOnly(true);
        this.cookie.setSecure(true);
        this.cookie.setDomain(env.getProperty("domain"));
        this.cookie.setPath("/api");
    }

    public Cookie getCookie(String name, String value, int expire){
        setCookie(name);
        this.cookie.setValue(value);
        this.cookie.setMaxAge(expire);

        return this.cookie;
    }

    public Cookie expireCookie(String name){
        setCookie(name);
        this.cookie.setMaxAge(0);

        return this.cookie;
    }


}
