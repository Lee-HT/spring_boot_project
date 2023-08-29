package com.example.demo.Converter;

import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public PostEntity toEntity(PostDto postDto) {
        return PostEntity.builder().pid(postDto.getPid()).title(postDto.getTitle())
                .contents(postDto.getContents())
                .build();
    }

    public PostDto toDto(PostEntity post) {
        return PostDto.builder().pid(post.getPid()).title(post.getTitle())
                .contents(post.getContents()).build();
    }

}
