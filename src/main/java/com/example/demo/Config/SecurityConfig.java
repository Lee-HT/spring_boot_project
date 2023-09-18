package com.example.demo.Config;

import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.OAuth2SuccessHandler;
import com.example.demo.Config.Oauth2.Oauth2LogoutHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final Oauth2LogoutHandler oauth2LogoutHandler;

    public SecurityConfig(OAuth2Service oAuth2Service, OAuth2SuccessHandler oAuth2SuccessHandler,
            Oauth2LogoutHandler oauth2LogoutHandler) {
        this.oAuth2Service = oAuth2Service;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oauth2LogoutHandler = oauth2LogoutHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        ); // 정적 리소스 시큐리티 예외 처리
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // SpringSecurity 6.1.2
        // https://docs.spring.io/spring-security/reference/migration-7/configuration.html
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        // 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login/login").permitAll()
                .anyRequest().permitAll()
        );
        http.logout(logout -> logout.logoutSuccessHandler(oauth2LogoutHandler));

        // spring security 의 authorization uri 생성 url
        // http://localhost:6550/oauth2/authorization/{registration_id}
        http.oauth2Login(oauth2 -> oauth2
                // oauth2 로그인 요청 uri
                .authorizationEndpoint(end -> end
                        .baseUri("/oauth2/authorization"))
                // redirect uri
                .redirectionEndpoint(end -> end
                        .baseUri("/login/oauth2/code/*"))
                .loginPage("/oauth2/authorization/google")
                .successHandler(oAuth2SuccessHandler)
                .defaultSuccessUrl("/")
                .userInfoEndpoint(userIEP -> userIEP.userService(oAuth2Service)));
        // 기존, 신규 세션 모두 사용 안함
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
