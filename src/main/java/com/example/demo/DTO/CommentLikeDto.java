package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentLikeDto {
    private boolean good;
    private boolean hate;

}
