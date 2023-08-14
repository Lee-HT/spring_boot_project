package com.example.demo.Domain;

import lombok.Builder;

@Builder
public class User {
    private Long id;
    private String username;
    private String email;

}
