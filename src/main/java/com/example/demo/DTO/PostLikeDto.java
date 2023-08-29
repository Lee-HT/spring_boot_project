package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeDto {
    private boolean good;
    private boolean hate;

}
