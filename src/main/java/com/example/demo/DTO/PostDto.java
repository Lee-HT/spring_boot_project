package com.example.demo.DTO;

import com.example.demo.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long pid;
    private Long uid;
    private String username;
    private String title;
    private String contents;
    private String category;
}
