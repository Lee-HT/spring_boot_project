package com.example.demo.Domain;

import lombok.Builder;

@Builder
public class Post {

    private String username;
    private String title;
    private String contents;

}
