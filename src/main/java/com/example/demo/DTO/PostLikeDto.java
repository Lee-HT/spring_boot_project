package com.example.demo.DTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeDto {
    private Long uid;
    private Long pid;
    // getter setter 차이 발생 Boolean : get set , boolean : is set
    private Boolean likes;

}
