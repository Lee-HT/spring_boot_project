package com.example.demo.DTO;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@ToString
public class PageDto {

    // 내용
    private List<PostDto> contents;
    // 총 페이지 수
    private int totalPages;
    // 한 페이지의 출력 개수
    private int size;
    // 현재 페이지 출력 개수
    private int numberOfElements;
    private Sort sorted;
}
