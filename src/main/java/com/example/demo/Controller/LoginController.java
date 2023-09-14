package com.example.demo.Controller;

import com.example.demo.Config.Oauth2.OAuth2Service;
import com.example.demo.Config.Oauth2.Oauth2CustomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("login")
public class LoginController {
    private final OAuth2Service oAuth2Service;
    private final Oauth2CustomService oauth2CustomService;
    @Autowired
    public LoginController(OAuth2Service oAuth2Service, Oauth2CustomService oauth2CustomService){
        this.oAuth2Service = oAuth2Service;
        this.oauth2CustomService = oauth2CustomService;
    }

    @GetMapping("/login")
    public String Login() {
        log.info("LoginController_Login");
        return "user/login";
    }

    // oauth2 redirect
    @GetMapping("/oauth2/code/{registrationId}")
    public void oauth2login(@RequestParam String code, @PathVariable String registrationId) {
        log.info("code : "+ code);
        log.info("registrationId : "+ registrationId);
        oauth2CustomService.socialLogin(code,registrationId);
    }
}
