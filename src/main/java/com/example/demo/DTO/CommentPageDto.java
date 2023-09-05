package com.example.demo.DTO;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@Builder
@ToString
public class CommentPageDto {

    private List<CommentDto> contents;
    private int totalPages;
    private int size;
    private int numberOfElements;
    private Sort sorted;

}
