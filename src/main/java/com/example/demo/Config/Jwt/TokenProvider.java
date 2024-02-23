package com.example.demo.Config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.lang.Assert;
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
public final class TokenProvider {

    private final Key key = JwtProperties.secretKey;

    // Token 만료 시간
    private Date expireTime(long expire) {
        Date now = new Date();
        return new Date(now.getTime() + expire);
    }

    // getClaims
    private Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private String getjwtToken(Date expire, String username, String provider) {
        Date now = new Date();

        // Header
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        // Payload Claims (key : value)
        Claims claims = Jwts.claims().setIssuedAt(now).setExpiration(expire);
        claims.put("role", "ROLE_USER");
        claims.put("prov", provider);

        String JwtToken = Jwts.builder().setHeader(headers).setClaims(claims).setSubject(username)
                .signWith(key).compact();

        // cookie 에 공백 사용 불가함으로 수정
        String tokenType = "bearer-";
        JwtToken = tokenType + JwtToken;

        return JwtToken;
    }

    public String getAccessToken(String username, String provider) {
        Date expire = expireTime(JwtProperties.accessTime);
        String JwtToken = getjwtToken(expire, username, provider);

        log.info(String.format("accessToken : %s", JwtToken));

        return JwtToken;
    }

    public String getRefreshToken(String username, String provider) {
        Date expire = expireTime(JwtProperties.refreshTime);
        String JwtToken = getjwtToken(expire, username, provider);

        log.info(String.format("refreshToken : %s", JwtToken));

        return JwtToken;
    }

    // claim 기반 authentication 객체 생성
    public Authentication getAuthentication(String token) {
        // payload parse 하여 claim 반환
        Claims claims = getClaims(token);
        String role = claims.get("role").toString();
        String provider = claims.get("prov").toString();

        // 권한 부여
        return new UsernamePasswordAuthenticationToken(provider, null,
                Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    // token 유효성 검증
    public Boolean validationToken(String token) {
        try {
            if (!token.isBlank()) {
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
    public String resolveCookie(Cookie cookie) {
        try{
            Assert.notNull(cookie,"cookie is null");
            if (!cookie.getValue().isBlank()) {
                return cookie.getValue().substring(7);
            }
            return "";
        }catch (Exception error){
            log.info(String.valueOf(error));
            return "";
        }
    }

    public String resolveToken(String token) {
        return token == null ? "" : token.substring(7);
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getProvider(String token) {
        Claims claims = getClaims(token);
        return (String) claims.get("prov");
    }
}
