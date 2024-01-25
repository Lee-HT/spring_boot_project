package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        HttpStatus status = HttpStatus.OK;
        UserDto updateUser = userService.updateUser(userDto);
        if (updateUser.getUid() == 0) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(userDto,status);
    }

    @DeleteMapping
    public Long deleteUser() {
        return userService.deleteUser();
    }
}
