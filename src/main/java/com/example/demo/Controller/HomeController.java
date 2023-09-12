package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("")
public class HomeController {

    @GetMapping("")
    public String Home() {
        log.info("HomeController_Home");
        return "main/home";
    }

    @PostMapping(value = "test",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody()
    public UserDto TestUri(UserDto userDto) {
        log.info("HomeController_Test");
        log.info(userDto.getUsername());
        return UserDto.builder().uid(userDto.getUid()).email(userDto.getEmail())
                .username(userDto.getUsername()).build();
    }

}
