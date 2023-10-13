package com.example.demo.Config.Jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public interface JwtProperties {
    String accessTokenName = "accessToken";
    String refreshTokenName = "refreshToken";
    String domain = System.getProperty("Domain");
    Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    Long refreshTime = 1000*3600*24*7L;
    Long accessTime = 1000*3600*24L;
}
