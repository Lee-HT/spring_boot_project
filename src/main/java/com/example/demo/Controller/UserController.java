package com.example.demo.Controller;

import com.example.demo.DTO.UserDto;
import com.example.demo.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long uid) {
        HttpStatus status = HttpStatus.OK;
        UserDto response = userService.findByUid(uid);
        if (response.getUid() == null){
            status = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(status);
        }
        return new ResponseEntity<>(response, status);
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        UserDto updateUser = userService.updateUser(userDto);
        if (updateUser.getUid() == 0) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        HttpStatus status = HttpStatus.NO_CONTENT;
        if (userService.deleteUser() == 0){
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(status);
    }
}
