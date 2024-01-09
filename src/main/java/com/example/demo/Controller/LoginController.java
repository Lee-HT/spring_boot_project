package com.example.demo.Controller;

import com.example.demo.Config.Oauth2.Oauth2CustomService;
import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping()
public class LoginController {
    private final Oauth2CustomService oauth2CustomService;
    private final UserService userService;

    @Autowired
    public LoginController(Oauth2CustomService oauth2CustomService, UserService userService) {
        this.oauth2CustomService = oauth2CustomService;
        this.userService = userService;
    }

    // user info
    @ResponseBody
    @GetMapping("/oauth2/userinfo")
    public UserDto getUserInfo(){
        return userService.getUidByProvider();
    }

    // oauth2 test
    // https://accounts.google.com/o/oauth2/v2/auth?client_id={client_id}&redirect_uri={redirect_uri}&response_type=code&scope={scope}
    @GetMapping("login/test/google")
    public String oauth2test() {
        return "redirect:https://accounts.google.com/o/oauth2/v2/auth?client_id=241034246573-r8a6mk2a53s9biah83n1hklutsrqni51.apps.googleusercontent.com"
                + "&redirect_uri=http://localhost:6550/login/oauth2/test/google&response_type=code&scope=email%20profile";
    }

    @GetMapping("login/oauth2/test/{registrationId}")
    @ResponseBody
    public String oauth2login(@RequestParam String code, @PathVariable String registrationId) {
        log.info("code : " + code);
        log.info("registrationId : " + registrationId);
        oauth2CustomService.socialLogin(code, registrationId);
        return "oauth2 success";
    }

}
