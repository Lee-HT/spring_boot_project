package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("")
public class ApiController {

    @GetMapping("")
    public ResponseEntity<?> HomePage(){
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("docs/index.html"));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 테스트용
    @PostMapping(value = "test",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public UserDto TestUri(UserDto userDto) {
        log.info(userDto.getUsername());
        return UserDto.builder().uid(userDto.getUid()).email(userDto.getEmail())
                .username(userDto.getUsername()).build();
    }

}
