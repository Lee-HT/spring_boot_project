package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class PostEntity extends BaseTimeEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String title;
    private String contents;

}
