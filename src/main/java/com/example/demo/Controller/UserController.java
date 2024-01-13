package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{uid}")
    public UserDto getUser(@PathVariable Long uid) {
        return userService.findByUid(uid);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping
    public Long deleteUser() {
        return userService.deleteUser();
    }
}
