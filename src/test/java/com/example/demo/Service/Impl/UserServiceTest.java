package com.example.demo.Service.Impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.Converter.UserConverter;
import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserConverter userConverter;
    @InjectMocks
    private UserServiceImpl userService;
    private List<UserEntity> users = new ArrayList<>();

    public UserServiceTest(){
        users.add(UserEntity.builder().username("user1").email("email1@gmail.com").build());
        users.add(UserEntity.builder().username("user2").email("email2@gmail.com").build());
    }

    @Test
    public void findByUsername(){
        String username = "user";
        when(userRepository.findByUsername(any(String.class))).thenReturn(users.get(0));
        UserEntity user = userService.findByUsername(username);

        Assertions.assertThat(user).isEqualTo(users.get(0));

        System.out.println("======== findByUsername ========");
        System.out.println(user);
    }

    @Test
    public void findByUsernameContaining(){
        String username = "user";
        when(userRepository.findByUsernameContaining(any(String.class))).thenReturn(this.users);
        List<UserEntity> users = userService.findByUsernameContaining(username);

        Assertions.assertThat(users).isEqualTo(this.users);

        System.out.println("======== findByUsernameContaining ========");
        System.out.println(users);
    }

    @Test
    public void saveUser(){
        UserDto userDto = UserDto.builder().email("email1@gmail.com").username("user1").build();
        when(userRepository.save(any(UserEntity.class))).thenReturn(users.get(0));
        when(userConverter.toEntity(any(UserDto.class))).thenReturn(users.get(0));
        UserEntity user = userService.saveUser(userDto);

        Assertions.assertThat(user).isEqualTo(users.get(0));

        System.out.println("======== saveUser ========");
        System.out.println(user);
    }

    @Test
    public void updateUser(){
        UserDto userDto = UserDto.builder().uid(1L).email("email1@gmail.com").username("user1").build();
        when(userRepository.findByUid(any(Long.class))).thenReturn(users.get(0));
        when(userConverter.toDto(any(UserEntity.class))).thenReturn(userDto);
        UserDto user = userService.updateUser(userDto);

        Assertions.assertThat(user).isEqualTo(userDto);

        System.out.println("======== updateUser ========");
        System.out.println(user);
    }

    @Test
    public void deleteUser(){
        List<Long> uid = Arrays.asList(1L,2L);
        for (Long i:uid) {
            when(userRepository.findByUid(i)).thenReturn(users.get(i.intValue()-1));
        }
        int count = userService.deleteUsers(uid);

        Assertions.assertThat(count).isEqualTo(2);
    }
}
