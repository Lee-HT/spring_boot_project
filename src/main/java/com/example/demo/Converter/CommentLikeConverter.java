package com.example.demo.Converter;

import com.example.demo.DTO.CommentLikeDto;
import com.example.demo.Entity.CommentEntity;
import com.example.demo.Entity.CommentLikeEntity;
import com.example.demo.Entity.UserEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeConverter {

    public CommentLikeEntity toEntity(
            CommentLikeDto commentLikeDto, UserEntity uid, CommentEntity cid) {
        return CommentLikeEntity.builder().likes(commentLikeDto.getLikes()).uid(uid).cid(cid)
                .build();
    }

    public List<CommentLikeEntity> toEntity(List<CommentLikeDto> commentLikeDto,
            List<UserEntity> uid, List<CommentEntity> cid) {
        List<CommentLikeEntity> commentLikeEntity = new ArrayList<>();
        for (int i = 0; i < commentLikeDto.size(); i++) {
            commentLikeEntity.add(toEntity(commentLikeDto.get(i), uid.get(i), cid.get(i)));
        }
        return commentLikeEntity;
    }

    public CommentLikeDto toDto(CommentLikeEntity commentLike) {
        return CommentLikeDto.builder().likes(commentLike.isLikes()).build();
    }

    public List<CommentLikeDto> toDto(List<CommentLikeEntity> commentLike) {
        List<CommentLikeDto> commentLikeDto = new ArrayList<>();
        for (CommentLikeEntity ett : commentLike) {
            commentLikeDto.add(toDto(ett));
        }
        return commentLikeDto;
    }

}
