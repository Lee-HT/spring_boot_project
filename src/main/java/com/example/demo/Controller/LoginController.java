package com.example.demo.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("")
public class LoginController {
    @GetMapping("login.login")
    public String Login(){
        log.info("LoginController_Login");
        return "user/login";
    }
}
