package com.example.demo.Config;

import com.example.demo.Config.Oauth2.OAuth2Service;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private OAuth2Service oAuth2Service;
    public SecurityConfig(OAuth2Service oAuth2Service){
        this.oAuth2Service = oAuth2Service;
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
        http.formLogin(formLogin -> formLogin
                .loginPage("/user/login").permitAll()
                .defaultSuccessUrl("/"));
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/post").hasRole("USER")
                .anyRequest().permitAll()
        );
        // jwt 구현을 위해 oauth2client 사용
        http.oauth2Client(Customizer.withDefaults());

        return http.build();
    }

    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
//            builder.addFilterBefore();
            super.configure(builder);
        }
    }
}
