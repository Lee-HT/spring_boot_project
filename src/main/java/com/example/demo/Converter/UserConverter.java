package com.example.demo.Converter;

import com.example.demo.DTO.UserDto;
import com.example.demo.DTO.UserPageDto;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserEntity toEntity(UserDto userDto) {
        return UserEntity.builder().uid(userDto.getUid())
                .username(userDto.getUsername()).email(userDto.getEmail())
                .profilePic(userDto.getProfilePic()).build();
    }

    public List<UserEntity> toEntity(List<UserDto> userDtos) {
        List<UserEntity> userEntity = new ArrayList<>();
        for (UserDto dto : userDtos) {
            userEntity.add(toEntity(dto));
        }
        return userEntity;
    }

    public UserDto toDto(UserEntity userEntity) {
        return UserDto.builder().uid(userEntity.getUid())
                .username(userEntity.getUsername()).email(userEntity.getEmail())
                .profilePic(userEntity.getProfilePic()).build();
    }

    public List<UserDto> toDto(List<UserEntity> userEntity) {
        List<UserDto> userDto = new ArrayList<>();
        for (UserEntity ett : userEntity) {
            userDto.add(toDto(ett));
        }
        return userDto;
    }

    public UserPageDto toDto(Page<UserEntity> pages) {
        return UserPageDto.builder().contents(toDto(pages.getContent()))
                .totalPages(pages.getTotalPages()).size(pages.getSize())
                .totalElements(pages.getTotalElements()).sorted(pages.getSort()).build();
    }

}
