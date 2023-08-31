package com.example.demo.Service;

import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
import java.util.List;

public interface UserService {


    UserEntity findByUsername(String username);

    List<UserEntity> findByUsernameContaining(String username);

    UserEntity saveUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    int deleteUsers(List<Long> uid);
}
