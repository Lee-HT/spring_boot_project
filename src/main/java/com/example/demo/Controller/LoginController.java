package com.example.demo.Controller;

import com.example.demo.Config.Jwt.JwtProperties;
import com.example.demo.Config.Oauth2.Oauth2CustomService;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.LoginService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping()
public class LoginController {

    private final Oauth2CustomService oauth2CustomService;
    private final UserService userService;
    private final LoginService loginService;
    private final Environment env;

    public LoginController(Oauth2CustomService oauth2CustomService, UserService userService,
            LoginService loginService, Environment env) {
        this.oauth2CustomService = oauth2CustomService;
        this.userService = userService;
        this.loginService = loginService;
        this.env = env;
    }

    // userInfo
    @GetMapping("/oauth2/userinfo")
    public ResponseEntity<UserDto> getUserInfo() {
        UserDto userDto = userService.findByProvider();
        HttpStatus status = HttpStatus.OK;
        if (userDto.getUid() == null) {
            status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(userDto, status);
    }

    // refresh accessToken
    @PostMapping("/oauth2/token")
    public ResponseEntity<String> getAccessToken(
            @CookieValue(value = JwtProperties.refreshTokenName, required = false) Cookie tokenCookie) {
        HttpStatus status = HttpStatus.CREATED;
        String accessToken = loginService.getAccessToken(tokenCookie);
        if (accessToken.isBlank()){
            status = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(status);
        }
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(JwtProperties.headerName,accessToken);
        return new ResponseEntity<>(headers, status);
    }

    // oauth2 test
    // https://accounts.google.com/o/oauth2/v2/auth?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code&scope={scope}
    @GetMapping("/login/test/google")
    public String oauth2test() {
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth?"
                + "client_id=241034246573-r8a6mk2a53s9biah83n1hklutsrqni51.apps.googleusercontent.com&redirect_uri="
                + "http://" + env.getProperty("domain") + ":6550/api/login/oauth2/test/google"
                + "&response_type=code&scope=email%20profile";
    }

    @GetMapping("/login/oauth2/test/{registrationId}")
    @ResponseBody
    // Path 임의 설정
    public String oauth2login(@RequestParam String code, @PathVariable String registrationId) {
        log.info("code : " + code);
        log.info("registrationId : " + registrationId);
        oauth2CustomService.socialLogin(code, registrationId);
        return "oauth2 success";
    }

}
