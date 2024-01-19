package com.example.demo.Config;

import com.example.demo.Config.Jwt.JwtAuthenticationFilter;
import com.example.demo.Config.Jwt.TokenProvider;
import com.example.demo.Config.Oauth2.OAuth2FailureHandler;
import com.example.demo.Config.Oauth2.OAuth2LogoutHandler;
import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.OAuth2SuccessHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Environment env;
    private final OAuth2Service oAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final OAuth2LogoutHandler oAuth2LogoutHandler;
    private final TokenProvider tokenProvider;

    public SecurityConfig(Environment env, OAuth2Service oAuth2Service,
            OAuth2SuccessHandler oAuth2SuccessHandler,
            OAuth2FailureHandler oAuth2FailureHandler, OAuth2LogoutHandler oAuth2LogoutHandler,
            TokenProvider tokenProvider) {
        this.env = env;
        this.oAuth2Service = oAuth2Service;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2LogoutHandler = oAuth2LogoutHandler;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                // 정적 리소스 시큐리티 예외 처리
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
                // 예외 처리 할 customURL ( 권장하지 않음 )
//                .requestMatchers(new AntPathRequestMatcher("/"));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            HandlerMappingIntrospector introspector) throws Exception {
        // SpringSecurity 6.1.2
        // https://docs.spring.io/spring-security/reference/migration-7/configuration.html
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(getConfigurationSource()));
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        // 기존, 신규 세션 모두 사용 안함
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(new AntPathRequestMatcher("/", "GET")).permitAll()

                .requestMatchers(requestMatchersAsArray(null, URLPattern.loginUrls)).permitAll()

                .requestMatchers(
                        requestMatchersConcatArray(
                                requestMatchersAsArray(HttpMethod.GET,
                                        URLPattern.permitAllGetMethod),
                                requestMatchersAsArray(HttpMethod.POST,
                                        URLPattern.permitAllPostMethod),
                                requestMatchersAsArray(HttpMethod.PUT,
                                        URLPattern.permitAllPutMethod))
                )
                .permitAll()
                .requestMatchers(
                        requestMatchersConcatArray(
                                requestMatchersAsArray(HttpMethod.GET,
                                        URLPattern.userGetMethod),
                                requestMatchersAsArray(HttpMethod.POST,
                                        URLPattern.userPostMethod),
                                requestMatchersAsArray(HttpMethod.DELETE,
                                        URLPattern.userDeleteMethod)))
                .hasAnyRole("USER")

                .anyRequest().hasAnyRole("USER")
        );

        // spring security 의 authorization uri 생성 uri
        // localhost:6550/oauth2/authorization/{registration_id}
        http.oauth2Login(oauth2 -> oauth2
                // oauth2 로그인 요청 uri
                .authorizationEndpoint(end -> end
                        .baseUri("/oauth2/authorization"))
                // redirect uri
                .redirectionEndpoint(end -> end
                        .baseUri("/login/oauth2/code/*"))
                //.loginPage("/login")
                // defaultSuccessUrl 과 동시 사용 x
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint(userIEP -> userIEP.userService(oAuth2Service)));

        http.logout(logout -> logout
                .logoutSuccessHandler(oAuth2LogoutHandler));

        http.addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        // 스레드 별로 SecurityContext 저장 (Default Mode)
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_THREADLOCAL);

        return http.build();
    }

    // CORS Configure
    CorsConfigurationSource getConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(env.getProperty("front")));
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // AntPathRequestMatcher 명시
    private RequestMatcher[] requestMatchersAsArray(HttpMethod httpMethod, String[] patterns) {
        String method = (httpMethod != null) ? httpMethod.toString() : null;
        RequestMatcher[] matchers = new RequestMatcher[patterns.length];
        for (int index = 0; index < patterns.length; index++) {
            matchers[index] = new AntPathRequestMatcher(patterns[index], method);
        }
        return matchers;
    }

    // RequestMatchers 병합
    private RequestMatcher[] requestMatchersConcatArray(RequestMatcher[]... requestMatchers) {
        List<RequestMatcher> concatArray = new ArrayList<>();
        for (RequestMatcher[] requestMatcher : requestMatchers) {
            concatArray.addAll(Arrays.asList(requestMatcher));
        }
        return concatArray.toArray(new RequestMatcher[0]);
    }

}
