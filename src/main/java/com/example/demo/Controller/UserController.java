package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("myPage")
    public String getUser(@RequestParam String username, Model model) {
        UserDto user = userService.findByUsername(username);

        Map<String,Object> attributes = new HashMap<>();
        attributes.put("username",user.getUsername());
        attributes.put("email",user.getEmail());
        attributes.put("profilePic",user.getProfilePic());
        model.addAllAttributes(attributes);

        return "user/myPage";
    }

    @PostMapping()
    public String saveUser(UserDto userDto) {
        UserDto user = userService.updateUser(userDto);
        return "user/info";
    }
}
