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
public class CommentPageDto {

    private List<CommentDto> contents;
    private Integer totalPages;
    private Integer size;
    private Integer numberOfElements;
    private Long totalElements;
    private Boolean sorted;

}
