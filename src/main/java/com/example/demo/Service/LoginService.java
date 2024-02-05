package com.example.demo.Service;

import jakarta.servlet.http.Cookie;

public interface LoginService {
    String getAccessToken(Cookie cookie);

}
