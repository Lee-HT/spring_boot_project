package com.example.demo.Converter;

import com.example.demo.DTO.PostLikeDto;
import com.example.demo.Entity.PostEntity;
import com.example.demo.Entity.PostLikeEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PostLikeConverter {

    public PostLikeEntity toEntity(PostLikeDto postLikeDto, UserEntity uid, PostEntity pid) {
        return PostLikeEntity.builder().likes(postLikeDto.getLikes()).uid(uid).pid(pid).build();
    }

    public List<PostLikeEntity> toEntity(List<PostLikeDto> postLikeDto, List<UserEntity> uid,
            List<PostEntity> pid) {
        List<PostLikeEntity> postLike = new ArrayList<>();
        for (int i = 0; i < postLikeDto.size(); i++) {
            PostLikeEntity.builder().likes(postLikeDto.get(0).getLikes()).uid(uid.get(i))
                    .pid(pid.get(i)).build();
        }
        return postLike;
    }

    public PostLikeDto toDto(PostLikeEntity postLike) {
        return PostLikeDto.builder().likes(postLike.getLikes()).uid(postLike.getUid().getUid())
                .pid(postLike.getPid().getPid()).build();
    }

    public List<PostLikeDto> toDto(List<PostLikeEntity> postLike){
        List<PostLikeDto> postLikeDto = new ArrayList<>();
        for (PostLikeEntity ett: postLike){
            postLikeDto.add(toDto(ett));
        }
        return postLikeDto;
    }
}
