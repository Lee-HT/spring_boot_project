package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDto {
    private String title;
    private String contents;

}
