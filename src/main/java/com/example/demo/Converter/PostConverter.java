package com.example.demo.Converter;

import com.example.demo.DTO.PostDto;
import com.example.demo.DTO.PostPageDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostConverter {

    public PostEntity toEntity(PostDto postDto, UserEntity userEntity) {
        return PostEntity.builder().pid(postDto.getPid()).uid(userEntity)
                .username(postDto.getUsername())
                .title(postDto.getTitle()).contents(postDto.getContents())
                .category(postDto.getCategory()).build();
    }

    public List<PostEntity> toEntity(List<PostDto> postDto, List<UserEntity> userEntity) {
        List<PostEntity> postEntity = new ArrayList<>();
        for (int i = 0; i < postDto.size(); i++) {
            postEntity.add(toEntity(postDto.get(i), userEntity.get(i)));
        }
        return postEntity;
    }

    public PostDto toDto(PostEntity post) {
        try {
            return PostDto.builder().pid(post.getPid()).uid(post.getUid().getUid())
                    .username(post.getUsername())
                    .title(post.getTitle()).contents(post.getContents()).category(post.getCategory())
                    .updatedAt(post.getUpdatedAt()).createdAt(post.getCreatedAt()).view(post.getView())
                    .build();
        } catch (Exception e) {
            log.info(e.toString());
        }
        return PostDto.builder().build();
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
        try {
            return PostPageDto.builder().contents(toDto(pages.getContent()))
                    .totalPages(pages.getTotalPages()).size(pages.getSize())
                    .numberOfElements(pages.getNumberOfElements())
                    .totalElements(pages.getTotalElements()).sorted(pages.getSort().isSorted()).build();
        } catch (Exception e){
            log.info(e.toString());
        }
        return PostPageDto.builder().build();

    }

}
