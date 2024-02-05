package com.example.demo.DTO;

import java.util.List;
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
public class PostPageDto {

    // 내용
    private List<PostDto> contents;
    // 총 페이지 수
    private Integer totalPages;
    // 한 페이지의 출력 개수
    private Integer size;
    // 현재 페이지 엘리먼트 출력 개수
    private Integer numberOfElements;
    // 총 엘리먼트 개수
    private Long totalElements;
    // 정렬 상태
    private Boolean sorted;
}
