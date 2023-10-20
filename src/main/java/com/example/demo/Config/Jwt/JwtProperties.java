package com.example.demo.Config.Jwt;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public interface JwtProperties {

    String accessTokenName = "accessToken";
    String refreshTokenName = "refreshToken";
    String domain = System.getProperty("Domain");
    //    Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    Key secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encodeToString(
                    "secretKKKeyiahweuigrhaeuighuihuiehauiewhuhaweuiheuiahwuhuihaweuifharuihurigeirgehaguiehrguierhguiahruihwwhiorhweaieohiohioawehtioehtiowaehtioawehtioehtiowaeht".getBytes())
            .getBytes());
    Long refreshTime = 1000 * 3600 * 24 * 7L;
    Long accessTime = 1000 * 3600 * 24L;
}
