package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("myPage")
    public String getUser(String username, Model model) {
        UserDto user = userService.findByUsername(username);
        return "user/myPage";
    }

    @PostMapping()
    public String saveUser(UserDto userDto) {
        UserDto user = userService.updateUser(userDto);
        return "user/info";
    }
}
