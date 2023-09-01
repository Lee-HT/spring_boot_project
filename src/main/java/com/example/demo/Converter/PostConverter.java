package com.example.demo.Converter;

import com.example.demo.DTO.PostPageDto;
import com.example.demo.DTO.PostDto;
import com.example.demo.Entity.PostEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PostConverter {

    public PostEntity toEntity(PostDto postDto) {
        return PostEntity.builder().pid(postDto.getPid()).title(postDto.getTitle())
                .contents(postDto.getContents()).category(postDto.getCategory()).build();
    }

    public List<PostEntity> toEntity(List<PostDto> postDto) {
        List<PostEntity> postEntity = new ArrayList<>();
        for (PostDto dto : postDto) {
            postEntity.add(toEntity(dto));
        }
        return postEntity;
    }

    public PostDto toDto(PostEntity post) {
        return PostDto.builder().pid(post.getPid()).title(post.getTitle())
                .contents(post.getContents()).category(post.getCategory()).build();
    }

    public List<PostDto> toDto(List<PostEntity> postEntity) {
        List<PostDto> postDto = new ArrayList<>();
        for (PostEntity ett : postEntity) {
            postDto.add(toDto(ett));
        }
        return postDto;
    }

    // 페이징
    public PostPageDto toDto(Page<PostEntity> pages) {
        return PostPageDto.builder().contents(toDto(pages.getContent()))
                .totalPages(pages.getTotalPages())
                .sorted(pages.getSort()).numberOfElements(pages.getNumberOfElements())
                .size(pages.getSize()).build();
    }

}
