package com.example.demo.Converter;

import com.example.demo.DTO.UserDto;
import com.example.demo.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserEntity toEntity(UserDto userDto) {
        return UserEntity.builder().uid(userDto.getUid()).username(userDto.getUsername())
                .email(userDto.getEmail()).build();
    }

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder().uid(userEntity.getUid()).username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .build();
    }

}
