package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
public class PostEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long postId;
    private String username;
    private String title;
    private String contents;

}
