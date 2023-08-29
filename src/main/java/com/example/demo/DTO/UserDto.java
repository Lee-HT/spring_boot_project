package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class UserDto {
    private Long uid;
    private String username;
    private String email;
    private String profilePic;

}
