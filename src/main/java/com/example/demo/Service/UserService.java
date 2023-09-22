package com.example.demo.Service;

import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {


    UserDto findByUid(Long Uid);

    UserPageDto findByUsernameContaining(String username, Pageable pageable);

    UserDto saveUser(UserDto userDto);

    UserDto updateUser(UserDto userDto);

    int deleteUsers(List<Long> uid);
}
