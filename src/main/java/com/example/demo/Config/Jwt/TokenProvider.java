package com.example.demo.Config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.Cookie;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenProvider {

    private static final String AutorizationHeader = "Authorization";
    private final Key key = JwtProperties.secretKey;
    private final String TokenType = "bearer ";
    private final long REFRESH_TOKEN_TIME = JwtProperties.refreshTime / 1000;
    private final long ACCESS_TOKEN_TIME = JwtProperties.accessTime / 1000;


    // Token 만료 시간
    private Date expireTime(long expire) {
        Date now = new Date();
        return new Date(now.getTime() + expire);
    }

    // getClaims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private String getjwtToken(Date expire, String username) {
        Date now = new Date();

        // Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        // Payload Claims (key : value)
        Claims claims = Jwts.claims().setIssuedAt(now).setExpiration(expire);
        claims.put("role", "ROLE_USER");

        String JwtToken = Jwts.builder().setHeader(headers).setClaims(claims).setSubject(username)
                .signWith(key).compact();

        JwtToken = TokenType + JwtToken;

        return JwtToken;
    }

    public String getAccessToken(String username) {
        Date expire = expireTime(ACCESS_TOKEN_TIME);

        String JwtToken = getjwtToken(expire, username);

        log.info(String.format("accessToken : %s", JwtToken));

        return JwtToken;
    }

    public String getRefreshToken(String username) {
        Date expire = expireTime(REFRESH_TOKEN_TIME);
        String JwtToken = getjwtToken(expire, username);

        log.info(String.format("refreshToken : %s", JwtToken));

        return JwtToken;
    }

    public Authentication getAuthentication(String token) {
        // payload parse 하여 claim 반환
        Claims claims = getClaims(token);
        String role = claims.get("role").toString();
        String username = claims.getSubject();

        // 권한 부여
        return new UsernamePasswordAuthenticationToken("username", null,
                Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    // token 유효성 검증
    public boolean validationToken(String token) {
        try {
            if (token != null) {
                Claims claims = getClaims(token);
                return claims.getExpiration().after(new Date());
            }
            return false;
        } catch (ExpiredJwtException e) {
            log.info("expire token");
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("not validate token");
        }
        return false;
    }

    // Cookie 에서 token get
    public Map<String, String> resolveToken(Cookie[] cookies) {
        HashMap<String, String> Tokens = new HashMap<>();
        try {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals("accessToken")) {
                    Tokens.put("accessToken", cookie.getValue());
                } else if (name.equals("refreshToken")) {
                    Tokens.put("refreshToken", cookie.getValue());
                }
            }
            return Tokens;
        } catch (Exception e) {
            log.info("resolve error");
        }
        return null;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
}
