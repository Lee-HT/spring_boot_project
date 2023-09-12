package com.example.demo.Controller;

import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.TokenService;
import com.example.demo.DTO.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {
    private OAuth2Service oAuth2Service;
    private TokenService tokenService;
    @Autowired
    public LoginController(OAuth2Service oAuth2Service,TokenService tokenService){
        this.oAuth2Service = oAuth2Service;
        this.tokenService = tokenService;
    }

    @GetMapping("login")
    public String Login(@RequestBody UserDto userDto) {
        log.info("LoginController_Login");
        return "user/login";
    }

    @GetMapping("/oauth2/code/{registrationId}")
    public void oauth2login(@RequestParam String code, @PathVariable String registrationId) {
        log.info("code : "+ code);
        log.info("registrationId : "+ registrationId);
        tokenService.socialLogin(code,registrationId);
    }
}
