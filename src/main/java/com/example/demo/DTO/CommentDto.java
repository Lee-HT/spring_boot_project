package com.example.demo.DTO;

import com.example.demo.Entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentDto {
    private Long cid;
    private Long uid;
    private String username;
    private String contents;
}
