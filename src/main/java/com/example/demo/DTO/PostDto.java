package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class PostDto {
    private Long pid;
    private String title;
    private String contents;
}
